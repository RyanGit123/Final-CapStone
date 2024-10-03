package com.techelevator.dao;
import com.techelevator.model.RentalAgreement;

import java.util.List;


public interface RentalAgreementDao {
    RentalAgreement addRentalAgreement(RentalAgreement rentalAgreement);


    RentalAgreement getRentalAgreementById(Long agreementId);

    //SS restored from backup
    RentalAgreement updateRentalAgreement(RentalAgreement rentalAgreement);

    int deleteRentalAgreement(Long id);
    List<RentalAgreement> getAllRentalAgreements();

    List<RentalAgreement> getRentalAgreementsByRenterId(Long renterId);

    List<RentalAgreement> getRentalAgreementsByLandlordId(Long landlordId);

    

}
