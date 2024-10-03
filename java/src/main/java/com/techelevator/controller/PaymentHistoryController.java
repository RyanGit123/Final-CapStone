package com.techelevator.controller;
import com.techelevator.dao.PaymentHistoryDao;
import com.techelevator.model.PaymentHistory;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin

public class PaymentHistoryController {
    private PaymentHistoryDao paymentHistoryDao;

    public PaymentHistoryController(PaymentHistoryDao paymentHistoryDao) {
        this.paymentHistoryDao = paymentHistoryDao;
    }

    @RequestMapping(path = "/payment_history/user/{userId}", method = RequestMethod.GET)
    public List<PaymentHistory> list(@PathVariable Long userId ) {
        return paymentHistoryDao.getAllPaymentsForUser(userId);
    }

    @RequestMapping(path = "/payment_history/{userId}/{propertyId}", method = RequestMethod.GET)
    public PaymentHistory get(@PathVariable Long userId, @PathVariable Long propertyId) {
        return paymentHistoryDao.getPaymentById(userId, propertyId);
    }


    //We need new added paynent as a return. remove void from payment history dao later and add Payment history return data type
        @ResponseStatus(HttpStatus.CREATED)
        @RequestMapping(path = "/payment_history", method = RequestMethod.POST)
        public PaymentHistory create(@RequestBody PaymentHistory paymentHistory) {
            return paymentHistoryDao.addPayment(paymentHistory);

        }



    // We should pass both the variable User id and property id as parameter along with payment history
    @RequestMapping(path = "/payment_history/{userId}/{propertyId}", method = RequestMethod.PUT)
    public ResponseEntity<PaymentHistory> update(@PathVariable Long userId, @PathVariable Long propertyId, @RequestBody PaymentHistory paymentHistory) {
        paymentHistory.setUserId(userId);
        paymentHistory.setPropertyId(propertyId);
        paymentHistoryDao.updatePayment(paymentHistory);
        return ResponseEntity.ok(paymentHistory);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/payment_history/{userId}/{propertyId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long userId, @PathVariable Long propertyId) {
         PaymentHistory existingPaymenyHistory = paymentHistoryDao.getPaymentById(userId, propertyId);
        if (existingPaymenyHistory != null) {
        paymentHistoryDao.deletePayment(userId, propertyId);
        }
    }

    @RequestMapping(path = "/payment_history/property/{propertyId}", method = RequestMethod.GET)
    public List<PaymentHistory> getPaymentsByPropertyId(@PathVariable Long propertyId) {
        return paymentHistoryDao.getAllPaymentsForProperty(propertyId);
    }

    @RequestMapping(path = "/payment_history", method = RequestMethod.GET)
    public List<PaymentHistory> list() {
        return paymentHistoryDao.getAllPaymentHistories();
    }
    @RequestMapping(path = "/payment_history/{id}", method = RequestMethod.GET)
    public PaymentHistory getPaymentById(@PathVariable Long id) {
        return paymentHistoryDao.getPaymentByPaymentId(id);
    }

}
