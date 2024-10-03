package com.techelevator.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentHistory {

    private Long paymentId;
    private Long userId;
    private Long propertyId;
    private int amountPaid;
    private int amountDue;
    private String rentersName;
    private int lateFee;
    private String paymentMethod;
    private LocalDate paymentDate;
    private LocalDate dueDate;

    public PaymentHistory() {}

    public PaymentHistory(Long paymentId, Long userId, Long propertyId, int amountPaid, int amountDue, String rentersName, int lateFee, String paymentMethod, LocalDate paymentDate, LocalDate dueDate) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.propertyId = propertyId;
        this.amountPaid = amountPaid;
        this.amountDue = amountDue;
        this.rentersName = rentersName;
        this.lateFee = lateFee;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.dueDate = dueDate;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(int amountDue) {
        this.amountDue = amountDue;
    }

    public String getRentersName() {
        return rentersName;
    }

    public void setRentersName(String rentersName) {
        this.rentersName = rentersName;
    }

    public int getLateFee() {
        return lateFee;
    }

    public void setLateFee(int lateFee) {
        this.lateFee = lateFee;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
