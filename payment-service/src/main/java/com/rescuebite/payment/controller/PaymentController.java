package com.rescuebite.payment.controller;

import com.rescuebite.payment.dto.PaymentResponse;
import com.rescuebite.payment.dto.ProcessPaymentRequest;
import com.rescuebite.payment.model.PaymentStatus;
import com.rescuebite.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment Management", description = "Endpoints for simulating payments, auditing transactions, refunds, and order payment coordination")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(summary = "Process simulated payment", description = "Simulates payment processing. Validates order amount with Order Service and marks order as CONFIRMED.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment data or order mismatch"),
            @ApiResponse(responseCode = "402", description = "Simulated payment failure (declined)")
    })
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody ProcessPaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Retrieves payment record by ID.")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by Order ID", description = "Retrieves the payment associated with a specific order.")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get payments by customer", description = "Retrieves all payment transactions completed by a customer.")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomerId(customerId));
    }

    @GetMapping
    @Operation(summary = "List all payments", description = "Returns all payment records, optionally filtered by status (SUCCESS, FAILED, REFUNDED).")
    public ResponseEntity<List<PaymentResponse>> getAllPayments(@RequestParam(required = false) PaymentStatus status) {
        return ResponseEntity.ok(paymentService.getAllPayments(status));
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "Refund payment", description = "Refunds a completed payment and cancels the associated order.")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable String id,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(paymentService.refundPayment(id, reason));
    }
}

