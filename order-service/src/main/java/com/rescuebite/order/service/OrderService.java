package com.rescuebite.order.service;

import com.rescuebite.order.dto.CreateOrderRequest;
import com.rescuebite.order.dto.ListingDto;
import com.rescuebite.order.dto.OrderResponse;
import com.rescuebite.order.exception.BadRequestException;
import com.rescuebite.order.exception.OrderConflictException;
import com.rescuebite.order.exception.ResourceNotFoundException;
import com.rescuebite.order.model.Order;
import com.rescuebite.order.model.OrderStatus;
import com.rescuebite.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestClient restClient;

    @Value("${services.listing-service.url:http://localhost:8083}")
    private String listingServiceUrl;

    @Value("${services.notification-service.url:http://localhost:8086}")
    private String notificationServiceUrl;

    public OrderService(OrderRepository orderRepository, RestClient restClient) {
        this.orderRepository = orderRepository;
        this.restClient = restClient;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        // 1. Fetch listing details from food-listing-service
        ListingDto listing;
        try {
            listing = restClient.get()
                    .uri(listingServiceUrl + "/api/listings/" + request.getListingId())
                    .retrieve()
                    .body(ListingDto.class);
        } catch (Exception e) {
            throw new BadRequestException("Unable to retrieve listing from Food Listing Service: " + e.getMessage());
        }

        if (listing == null) {
            throw new ResourceNotFoundException("Listing not found with ID: " + request.getListingId());
        }

        if (!"AVAILABLE".equalsIgnoreCase(listing.getStatus())) {
            throw new OrderConflictException("This listing is no longer available (Status: " + listing.getStatus() + ")");
        }

        if (listing.getQuantity() < request.getQuantity()) {
            throw new OrderConflictException("Requested quantity (" + request.getQuantity() + ") exceeds available stock (" + listing.getQuantity() + ")");
        }

        // 2. Reduce stock atomically in food-listing-service
        try {
            Map<String, Integer> stockPayload = Collections.singletonMap("quantity", request.getQuantity());
            restClient.patch()
                    .uri(listingServiceUrl + "/api/listings/" + request.getListingId() + "/reduce-stock")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(stockPayload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new OrderConflictException("Failed to reserve stock: " + e.getMessage());
        }

        // 3. Compute pricing and initial status
        double unitPrice = listing.isDonation() ? 0.0 : listing.getDiscountedPrice();
        double totalAmount = unitPrice * request.getQuantity();
        OrderStatus initialStatus = listing.isDonation() ? OrderStatus.CONFIRMED : OrderStatus.PENDING_PAYMENT;
        String collectionCode = generateCollectionCode();

        Order order = new Order(
                request.getCustomerId(),
                request.getCustomerName(),
                request.getCustomerEmail(),
                listing.getId(),
                listing.getTitle(),
                listing.getProviderId(),
                request.getQuantity(),
                unitPrice,
                totalAmount,
                listing.isDonation(),
                initialStatus,
                collectionCode,
                listing.getPickupAddress(),
                listing.getCollectionDeadline()
        );

        Order saved = orderRepository.save(order);

        // 4. Dispatch notification
        dispatchNotification(
                saved.getCustomerId(),
                "Order Created: " + saved.getListingTitle(),
                "Your order #" + saved.getId() + " is placed with status " + saved.getStatus() + ". Pickup Code: " + saved.getCollectionCode(),
                "ORDER"
        );

        return new OrderResponse(saved);
    }

    public OrderResponse getOrderById(String id) {
        Order order = findOrderById(id);
        return new OrderResponse(order);
    }

    public List<OrderResponse> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByProvider(String providerId) {
        return orderRepository.findByProviderId(providerId).stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrders(OrderStatus status) {
        List<Order> list = status != null ? orderRepository.findByStatus(status) : orderRepository.findAll();
        return list.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(String id, OrderStatus status) {
        Order order = findOrderById(id);
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        Order updated = orderRepository.save(order);

        dispatchNotification(
                updated.getCustomerId(),
                "Order Status Updated",
                "Your order #" + updated.getId() + " status is now " + status,
                "ORDER"
        );

        return new OrderResponse(updated);
    }

    public OrderResponse cancelOrder(String id) {
        Order order = findOrderById(id);

        if (order.getStatus() == OrderStatus.COLLECTED) {
            throw new OrderConflictException("Cannot cancel an order that has already been collected.");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderConflictException("Order is already cancelled.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        Order cancelled = orderRepository.save(order);

        // Restore stock in food-listing-service
        try {
            Map<String, Integer> restorePayload = Collections.singletonMap("quantity", order.getQuantity());
            restClient.patch()
                    .uri(listingServiceUrl + "/api/listings/" + order.getListingId() + "/restore-stock")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(restorePayload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            System.out.println("Warning: Could not notify food-listing-service to restore stock: " + e.getMessage());
        }

        dispatchNotification(
                cancelled.getCustomerId(),
                "Order Cancelled",
                "Your order #" + cancelled.getId() + " has been cancelled.",
                "ORDER"
        );

        return new OrderResponse(cancelled);
    }

    public OrderResponse collectOrder(String id, String collectionCode) {
        Order order = findOrderById(id);

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new OrderConflictException("Order must be in CONFIRMED status before it can be collected. Current status: " + order.getStatus());
        }

        if (!order.getCollectionCode().equalsIgnoreCase(collectionCode.trim())) {
            throw new BadRequestException("Invalid collection code provided.");
        }

        order.setStatus(OrderStatus.COLLECTED);
        order.setUpdatedAt(LocalDateTime.now());
        Order collected = orderRepository.save(order);

        dispatchNotification(
                collected.getCustomerId(),
                "Order Collected Successfully",
                "You have collected your order #" + collected.getId() + ". Thank you for reducing food waste with RescueBite!",
                "ORDER"
        );

        return new OrderResponse(collected);
    }

    private Order findOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }

    private String generateCollectionCode() {
        return "RB-" + (1000 + new Random().nextInt(9000));
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
            System.out.println("Notification dispatch deferred (Notification service offline): " + e.getMessage());
        }
    }
}

