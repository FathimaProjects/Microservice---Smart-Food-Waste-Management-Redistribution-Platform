package com.rescuebite.order.dto;

import com.rescuebite.order.model.Order;
import com.rescuebite.order.model.OrderStatus;

import java.time.LocalDateTime;

public class OrderResponse {

    private String id;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String listingId;
    private String listingTitle;
    private String providerId;
    private int quantity;
    private double unitPrice;
    private double totalAmount;
    private boolean donation;
    private OrderStatus status;
    private String collectionCode;
    private String pickupAddress;
    private LocalDateTime collectionDeadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderResponse() {
    }

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.customerId = order.getCustomerId();
        this.customerName = order.getCustomerName();
        this.customerEmail = order.getCustomerEmail();
        this.listingId = order.getListingId();
        this.listingTitle = order.getListingTitle();
        this.providerId = order.getProviderId();
        this.quantity = order.getQuantity();
        this.unitPrice = order.getUnitPrice();
        this.totalAmount = order.getTotalAmount();
        this.donation = order.isDonation();
        this.status = order.getStatus();
        this.collectionCode = order.getCollectionCode();
        this.pickupAddress = order.getPickupAddress();
        this.collectionDeadline = order.getCollectionDeadline();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isDonation() {
        return donation;
    }

    public void setDonation(boolean donation) {
        this.donation = donation;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public LocalDateTime getCollectionDeadline() {
        return collectionDeadline;
    }

    public void setCollectionDeadline(LocalDateTime collectionDeadline) {
        this.collectionDeadline = collectionDeadline;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

