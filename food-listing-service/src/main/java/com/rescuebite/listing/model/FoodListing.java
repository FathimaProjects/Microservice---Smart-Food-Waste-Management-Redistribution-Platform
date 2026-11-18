package com.rescuebite.listing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "food_listings")
public class FoodListing {

    @Id
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

    private ListingStatus status = ListingStatus.AVAILABLE;

    private LocalDateTime expiryDate;

    private LocalDateTime collectionDeadline;

    private String pickupAddress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public FoodListing() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = ListingStatus.AVAILABLE;
    }

    public FoodListing(String providerId, String providerName, String title, String description,
                       FoodCategory category, int quantity, String unit, double originalPrice,
                       double discountedPrice, boolean donation, LocalDateTime expiryDate,
                       LocalDateTime collectionDeadline, String pickupAddress) {
        this();
        this.providerId = providerId;
        this.providerName = providerName;
        this.title = title;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.originalPrice = originalPrice;
        this.discountedPrice = donation ? 0.0 : discountedPrice;
        this.donation = donation;
        this.expiryDate = expiryDate;
        this.collectionDeadline = collectionDeadline;
        this.pickupAddress = pickupAddress;
        this.status = quantity > 0 ? ListingStatus.AVAILABLE : ListingStatus.OUT_OF_STOCK;
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

