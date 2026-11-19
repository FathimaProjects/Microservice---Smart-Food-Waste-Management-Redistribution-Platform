package com.rescuebite.payment.dto;

import com.rescuebite.payment.model.Payment;
import com.rescuebite.payment.model.PaymentMethod;
import com.rescuebite.payment.model.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentResponse {

    private String id;
    private String orderId;
    private String customerId;
    private double amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private String transactionReference;
    private PaymentStatus status;
    private String cardLastFour;
    private String failureReason;
    private LocalDateTime paymentDate;

    public PaymentResponse() {
    }

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.orderId = payment.getOrderId();
        this.customerId = payment.getCustomerId();
        this.amount = payment.getAmount();
        this.currency = payment.getCurrency();
        this.paymentMethod = payment.getPaymentMethod();
        this.transactionReference = payment.getTransactionReference();
        this.status = payment.getStatus();
        this.cardLastFour = payment.getCardLastFour();
        this.failureReason = payment.getFailureReason();
        this.paymentDate = payment.getPaymentDate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getCardLastFour() {
        return cardLastFour;
    }

    public void setCardLastFour(String cardLastFour) {
        this.cardLastFour = cardLastFour;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}

