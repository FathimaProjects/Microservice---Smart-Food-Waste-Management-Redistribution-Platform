package com.rescuebite.provider.dto;

import com.rescuebite.provider.model.VerificationStatus;
import jakarta.validation.constraints.NotNull;

public class VerifyProviderRequest {

    @NotNull(message = "Verification status is required (VERIFIED, REJECTED, PENDING)")
    private VerificationStatus status;

    private String rejectionReason;

    public VerifyProviderRequest() {
    }

    public VerifyProviderRequest(VerificationStatus status, String rejectionReason) {
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    public VerificationStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}

