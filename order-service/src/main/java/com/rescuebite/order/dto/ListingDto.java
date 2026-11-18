package com.rescuebite.order.dto;

import java.time.LocalDateTime;

public class ListingDto {

    private String id;
    private String providerId;
    private String providerName;
    private String title;
    private int quantity;
    private double originalPrice;
    private double discountedPrice;
    private boolean donation;
    private String status;
    private LocalDateTime collectionDeadline;
    private String pickupAddress;

    public ListingDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public boolean isDonation() {
        return donation;
    }

    public void setDonation(boolean donation) {
        this.donation = donation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCollectionDeadline() {
        return collectionDeadline;
    }

    public void setCollectionDeadline(LocalDateTime collectionDeadline) {
        this.collectionDeadline = collectionDeadline;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }
}

