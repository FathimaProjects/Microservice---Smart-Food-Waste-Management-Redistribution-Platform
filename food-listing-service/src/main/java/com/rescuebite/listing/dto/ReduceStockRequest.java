package com.rescuebite.listing.dto;

import jakarta.validation.constraints.Min;

public class ReduceStockRequest {

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    public ReduceStockRequest() {
    }

    public ReduceStockRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

