package com.rescuebite.listing.dto;

import com.rescuebite.listing.model.FoodCategory;
import com.rescuebite.listing.model.FoodListing;
import com.rescuebite.listing.model.ListingStatus;

import java.time.LocalDateTime;

public class ListingResponse {

    private String id;
    private String providerId;
    private String providerName;
    private String title;
    private String description;
    private FoodCategory category;
    private int quantity;
    private String unit;
    private double originalPrice;
    private double discountedPrice;
    private boolean donation;
    private ListingStatus status;
    private LocalDateTime expiryDate;
    private LocalDateTime collectionDeadline;
    private String pickupAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ListingResponse() {
    }

    public ListingResponse(FoodListing listing) {
        this.id = listing.getId();
        this.providerId = listing.getProviderId();
        this.providerName = listing.getProviderName();
        this.title = listing.getTitle();
        this.description = listing.getDescription();
        this.category = listing.getCategory();
        this.quantity = listing.getQuantity();
        this.unit = listing.getUnit();
        this.originalPrice = listing.getOriginalPrice();
        this.discountedPrice = listing.getDiscountedPrice();
        this.donation = listing.isDonation();
        this.status = listing.getStatus();
        this.expiryDate = listing.getExpiryDate();
        this.collectionDeadline = listing.getCollectionDeadline();
        this.pickupAddress = listing.getPickupAddress();
        this.createdAt = listing.getCreatedAt();
        this.updatedAt = listing.getUpdatedAt();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public ListingStatus getStatus() {
        return status;
    }

    public void setStatus(ListingStatus status) {
        this.status = status;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
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

