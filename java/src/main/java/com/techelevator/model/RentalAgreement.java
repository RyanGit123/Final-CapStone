package com.techelevator.model;

import java.time.LocalDateTime;

public class RentalAgreement {
    private Long agreementId;
    private Long renterId;
    private Long landlordId;
    private Long propertyId;
    private LocalDateTime leaseStartDate;
    private LocalDateTime leaseEndDate;
    private int rentAmount;


public RentalAgreement() {}


    public RentalAgreement(Long agreementId, Long renterId, Long landlordId, Long propertyId, LocalDateTime leaseStartDate, LocalDateTime leaseEndDate, int rentAmount) {
        this.agreementId = agreementId;
        this.renterId = renterId;
        this.landlordId = landlordId;
        this.propertyId = propertyId;
        this.leaseStartDate = leaseStartDate;
        this.leaseEndDate = leaseEndDate;
        this.rentAmount = rentAmount;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public Long getRenterId() {
        return renterId;
    }

    public void setRenterId(Long renterId) {
        this.renterId = renterId;
    }

    public Long getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(Long landlordId) {
        this.landlordId = landlordId;
    }

    public LocalDateTime getLeaseStartDate() {
        return leaseStartDate;
    }

    public void setLeaseStartDate(LocalDateTime leaseStartDate) {
        this.leaseStartDate = leaseStartDate;
    }

    public LocalDateTime getLeaseEndDate() {
        return leaseEndDate;
    }

    public void setLeaseEndDate(LocalDateTime leaseEndDate) {
        this.leaseEndDate = leaseEndDate;
    }

    public int getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(int rentAmount) {
        this.rentAmount = rentAmount;
    }
    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
     
}

