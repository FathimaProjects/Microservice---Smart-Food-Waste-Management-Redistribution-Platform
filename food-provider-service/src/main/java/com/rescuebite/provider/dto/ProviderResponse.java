package com.rescuebite.provider.dto;

import com.rescuebite.provider.model.BusinessType;
import com.rescuebite.provider.model.FoodProvider;
import com.rescuebite.provider.model.VerificationStatus;

import java.time.LocalDateTime;

public class ProviderResponse {

    private String id;
    private String userId;
    private String businessName;
    private BusinessType businessType;
    private String registrationNumber;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private String city;
    private String operatingHours;
    private VerificationStatus verificationStatus;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProviderResponse() {
    }

    public ProviderResponse(FoodProvider provider) {
        this.id = provider.getId();
        this.userId = provider.getUserId();
        this.businessName = provider.getBusinessName();
        this.businessType = provider.getBusinessType();
        this.registrationNumber = provider.getRegistrationNumber();
        this.contactEmail = provider.getContactEmail();
        this.contactPhone = provider.getContactPhone();
        this.address = provider.getAddress();
        this.city = provider.getCity();
        this.operatingHours = provider.getOperatingHours();
        this.verificationStatus = provider.getVerificationStatus();
        this.rejectionReason = provider.getRejectionReason();
        this.createdAt = provider.getCreatedAt();
        this.updatedAt = provider.getUpdatedAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
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

