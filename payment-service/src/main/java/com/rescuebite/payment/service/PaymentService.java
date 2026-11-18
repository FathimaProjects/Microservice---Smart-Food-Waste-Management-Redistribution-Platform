package com.rescuebite.payment.service;

import com.rescuebite.payment.dto.OrderDto;
import com.rescuebite.payment.dto.PaymentResponse;
import com.rescuebite.payment.dto.ProcessPaymentRequest;
import com.rescuebite.payment.exception.BadRequestException;
import com.rescuebite.payment.exception.PaymentProcessingException;
import com.rescuebite.payment.exception.ResourceNotFoundException;
import com.rescuebite.payment.model.Payment;
import com.rescuebite.payment.model.PaymentStatus;
import com.rescuebite.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RestClient restClient;

    @Value("${services.order-service.url:http://localhost:8084}")
    private String orderServiceUrl;

    @Value("${services.notification-service.url:http://localhost:8086}")
    private String notificationServiceUrl;

    public PaymentService(PaymentRepository paymentRepository, RestClient restClient) {
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
    }

    public PaymentResponse processPayment(ProcessPaymentRequest request) {
        // Prevent duplicate payment for the same order
        Optional<Payment> existingSuccess = paymentRepository.findByOrderId(request.getOrderId());
        if (existingSuccess.isPresent() && existingSuccess.get().getStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("Order " + request.getOrderId() + " has already been paid for.");
        }

        // 1. Verify order details via order-service
        OrderDto order;
        try {
            order = restClient.get()
                    .uri(orderServiceUrl + "/api/orders/" + request.getOrderId())
                    .retrieve()
                    .body(OrderDto.class);
        } catch (Exception e) {
            throw new BadRequestException("Unable to retrieve order from Order Service: " + e.getMessage());
        }

        if (order == null) {
            throw new ResourceNotFoundException("Order not found with ID: " + request.getOrderId());
        }

        if (!"PENDING_PAYMENT".equalsIgnoreCase(order.getStatus())) {
            throw new BadRequestException("Order cannot be paid. Current status: " + order.getStatus());
        }

        if (Math.abs(order.getTotalAmount() - request.getAmount()) > 0.01) {
            throw new BadRequestException("Payment amount (" + request.getAmount() + ") does not match order total (" + order.getTotalAmount() + ")");
        }

        String txnRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 2. Simulated payment failure check
        if (request.isSimulateFailure()) {
            Payment failedPayment = new Payment(
                    request.getOrderId(),
                    request.getCustomerId(),
                    request.getAmount(),
                    request.getCurrency(),
                    request.getPaymentMethod(),
                    txnRef,
                    PaymentStatus.FAILED,
                    request.getCardLastFour() != null ? request.getCardLastFour() : "XXXX"
            );
            failedPayment.setFailureReason("Simulated card decline / insufficient balance.");
            paymentRepository.save(failedPayment);

            dispatchNotification(
                    request.getCustomerId(),
                    "Payment Failed",
                    "Payment of " + request.getAmount() + " " + request.getCurrency() + " for order #" + request.getOrderId() + " failed.",
                    "PAYMENT"
            );

            throw new PaymentProcessingException("Payment transaction failed: Card declined / simulated failure.");
        }

        // 3. Process simulated successful payment
        Payment payment = new Payment(
                request.getOrderId(),
                request.getCustomerId(),
                request.getAmount(),
                request.getCurrency(),
                request.getPaymentMethod(),
                txnRef,
                PaymentStatus.SUCCESS,
                request.getCardLastFour() != null ? request.getCardLastFour() : "4242"
        );

        Payment savedPayment = paymentRepository.save(payment);

        // 4. Update order status to CONFIRMED in order-service
        try {
            Map<String, String> statusPayload = Collections.singletonMap("status", "CONFIRMED");
            restClient.patch()
                    .uri(orderServiceUrl + "/api/orders/" + request.getOrderId() + "/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(statusPayload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            System.out.println("Warning: Failed to update order status in order-service: " + e.getMessage());
        }

        // 5. Notify customer
        dispatchNotification(
                savedPayment.getCustomerId(),
                "Payment Successful",
                "Your payment of " + savedPayment.getAmount() + " " + savedPayment.getCurrency() +
                        " was successful! Reference: " + savedPayment.getTransactionReference(),
                "PAYMENT"
        );

        return new PaymentResponse(savedPayment);
    }

    public PaymentResponse getPaymentById(String id) {
        Payment payment = findPaymentById(id);
        return new PaymentResponse(payment);
    }

    public PaymentResponse getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found for Order ID: " + orderId));
        return new PaymentResponse(payment);
    }

    public List<PaymentResponse> getPaymentsByCustomerId(String customerId) {
        return paymentRepository.findByCustomerId(customerId).stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getAllPayments(PaymentStatus status) {
        List<Payment> list = status != null ? paymentRepository.findByStatus(status) : paymentRepository.findAll();
        return list.stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    public PaymentResponse refundPayment(String id, String reason) {
        Payment payment = findPaymentById(id);

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BadRequestException("Only successful payments can be refunded. Current status: " + payment.getStatus());
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setFailureReason("Refunded: " + (reason != null ? reason : "Customer cancellation"));
        payment.setUpdatedAt(LocalDateTime.now());
        Payment refunded = paymentRepository.save(payment);

        // Cancel order in order-service
        try {
            Map<String, String> statusPayload = Collections.singletonMap("status", "CANCELLED");
            restClient.patch()
                    .uri(orderServiceUrl + "/api/orders/" + refunded.getOrderId() + "/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(statusPayload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            System.out.println("Warning: Could not update order-service status on refund: " + e.getMessage());
        }

        dispatchNotification(
                refunded.getCustomerId(),
                "Payment Refunded",
                "Your payment of " + refunded.getAmount() + " " + refunded.getCurrency() + " has been refunded.",
                "PAYMENT"
        );

        return new PaymentResponse(refunded);
    }

    private Payment findPaymentById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
    }

    private void dispatchNotification(String userId, String title, String message, String type) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("userId", userId);
            payload.put("title", title);
            payload.put("message", message);
            payload.put("type", type);

            restClient.post()
                    .uri(notificationServiceUrl + "/api/notifications")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            System.out.println("Notification dispatch deferred: " + e.getMessage());
        }
    }
}

