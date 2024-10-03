package com.techelevator.controller;
import com.techelevator.model.RentalAgreement;
import com.techelevator.dao.RentalAgreementDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import com.techelevator.model.RentalAgreementDao;

@RestController
@CrossOrigin
public class RentalAgreementController {
    private final RentalAgreementDao rentalAgreementDao;

    public RentalAgreementController(RentalAgreementDao rentalAgreementDao) {
        this.rentalAgreementDao = rentalAgreementDao;
    }

    @RequestMapping(path = "/rental_agreements", method = RequestMethod.GET)
    public List<RentalAgreement> list() {
        return rentalAgreementDao.getAllRentalAgreements();
    }

    /**
     * Return Rental Agreement Information
     * @param id the id of the rental agreement
     * @return all the information of the given rental agreement
     */
    @RequestMapping(path = "/rental_agreements/{id}", method = RequestMethod.GET)
    public RentalAgreement get(@PathVariable Long id) {
        return rentalAgreementDao.getRentalAgreementById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/rental_agreements", method = RequestMethod.POST)
    public RentalAgreement create(@RequestBody RentalAgreement rentalAgreement) {
        return rentalAgreementDao.addRentalAgreement(rentalAgreement);
        // return ResponseEntity.ok(rentalAgreement);
    }

    @RequestMapping(path = "/rental_agreements/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RentalAgreement> update(@PathVariable Long id, @RequestBody RentalAgreement rentalAgreement) {
        rentalAgreement.setAgreementId(id);
        rentalAgreementDao.updateRentalAgreement(rentalAgreement);
        return ResponseEntity.ok(rentalAgreement);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/rental_agreements/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        rentalAgreementDao.deleteRentalAgreement(id);
        //return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/rental_agreements/renter/{renterId}", method = RequestMethod.GET)
    public List<RentalAgreement> getRentalAgreementsByRenterId(@PathVariable Long renterId) {
        return rentalAgreementDao.getRentalAgreementsByRenterId(renterId);
    }

    @RequestMapping(path = "/rental_agreements/landlord/{landlordId}", method = RequestMethod.GET)
    public List<RentalAgreement> getRentalAgreementsByLandlordId(@PathVariable Long landlordId) {
        return rentalAgreementDao.getRentalAgreementsByLandlordId(landlordId);
    }
}