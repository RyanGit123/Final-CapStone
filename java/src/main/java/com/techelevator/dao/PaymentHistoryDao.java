package com.techelevator.dao;

import com.techelevator.model.PaymentHistory;

import java.util.List;

public interface PaymentHistoryDao {
     PaymentHistory getPaymentByPaymentId(Long id);

    PaymentHistory addPayment(PaymentHistory paymentHistory);

    PaymentHistory getPaymentById(Long userId, Long propertyId);

    List<PaymentHistory> getAllPaymentsForUser(Long userId);

    void updatePayment(PaymentHistory paymentHistory);

    void deletePayment(Long userId, Long propertyId);

    List<PaymentHistory> getAllPaymentsForProperty(Long propertyId);

    List<PaymentHistory> getAllPaymentHistories();

}
