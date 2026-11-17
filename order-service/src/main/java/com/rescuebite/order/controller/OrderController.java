package com.rescuebite.order.controller;

import com.rescuebite.order.dto.CreateOrderRequest;
import com.rescuebite.order.dto.OrderResponse;
import com.rescuebite.order.dto.UpdateOrderStatusRequest;
import com.rescuebite.order.model.OrderStatus;
import com.rescuebite.order.service.OrderService;
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
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "Endpoints for placing orders, tracking order statuses, cancellations, and collections")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Place a new order", description = "Reserves surplus food item, verifies stock with Food Listing Service, and generates a collection code.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or stock unavailable"),
            @ApiResponse(responseCode = "409", description = "Stock insufficient or conflict")
    })
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves full details of a specific order.")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get orders by customer", description = "Retrieves order history for a customer or charity.")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    @GetMapping("/provider/{providerId}")
    @Operation(summary = "Get orders by provider", description = "Retrieves incoming surplus orders for a specific food provider.")
    public ResponseEntity<List<OrderResponse>> getOrdersByProvider(@PathVariable String providerId) {
        return ResponseEntity.ok(orderService.getOrdersByProvider(providerId));
    }

    @GetMapping
    @Operation(summary = "List all orders", description = "Returns all platform orders, optionally filtered by status.")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestParam(required = false) OrderStatus status) {
        return ResponseEntity.ok(orderService.getAllOrders(status));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Updates order status (e.g. called by Payment Service to transition PENDING_PAYMENT to CONFIRMED).")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request.getStatus()));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancels an order and automatically restores reserved stock in Food Listing Service.")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @PostMapping("/{id}/collect")
    @Operation(summary = "Verify and collect order", description = "Food provider verifies customer's 6-digit collection code to complete order collection.")
    public ResponseEntity<OrderResponse> collectOrder(
            @PathVariable String id,
            @RequestParam String collectionCode) {
        return ResponseEntity.ok(orderService.collectOrder(id, collectionCode));
    }
}

