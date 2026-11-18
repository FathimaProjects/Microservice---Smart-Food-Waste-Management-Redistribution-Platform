package com.rescuebite.order.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "orders")
public class Order {

    @Id
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

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Order(String customerId, String customerName, String customerEmail,
                 String listingId, String listingTitle, String providerId,
                 int quantity, double unitPrice, double totalAmount,
                 boolean donation, OrderStatus status, String collectionCode,
                 String pickupAddress, LocalDateTime collectionDeadline) {
        this();
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.listingId = listingId;
        this.listingTitle = listingTitle;
        this.providerId = providerId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.donation = donation;
        this.status = status;
        this.collectionCode = collectionCode;
        this.pickupAddress = pickupAddress;
        this.collectionDeadline = collectionDeadline;
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

