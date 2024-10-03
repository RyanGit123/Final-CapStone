package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.PaymentHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPaymentHistoryDao implements PaymentHistoryDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcPaymentHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public PaymentHistory getPaymentByPaymentId(Long id) {
        PaymentHistory paymentHistory = null;
        String sql = "SELECT payment_id, user_id, property_id, amount_paid, amount_due, renters_name, late_fee, payment_method, payment_date, due_date " +
                "FROM payment_history WHERE payment_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                paymentHistory = mapRowToPaymentHistory(results);
            }
        } catch (Exception e) {
            throw new DaoException("Failed to retrieve payment history", e);
        }
        return paymentHistory;
    }



    @Override
    public PaymentHistory addPayment(PaymentHistory paymentHistory) {
        String sql = "INSERT INTO payment_history (user_id, property_id, amount_paid, amount_due, renters_name, late_fee, payment_method, payment_date, due_date)  " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING payment_id";
                PaymentHistory newPaymentHistory = null;
        try {
            Long newId = jdbcTemplate.queryForObject(sql, Long.class,
                    paymentHistory.getUserId(),
                    paymentHistory.getPropertyId(),
                    paymentHistory.getAmountPaid(),
                    paymentHistory.getAmountDue(),
                    paymentHistory.getRentersName(),
                    paymentHistory.getLateFee(),
                    paymentHistory.getPaymentMethod(),
                    paymentHistory.getPaymentDate(),
                    paymentHistory.getDueDate());
                    PaymentHistory newPayment = getPaymentByPaymentId(newId);
                    return newPayment;

        }  catch (Exception e) {
            throw new DaoException("Failed to add payment history", e);
        }
    }

    @Override
    public PaymentHistory getPaymentById(Long userId, Long propertyId) {
        String sql = "SELECT payment_id, user_id, property_id, amount_paid, amount_due, renters_name, late_fee, payment_method, payment_date, due_date " +
                "FROM payment_history WHERE user_id = ? AND property_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, propertyId);
            if (results.next()) {
                return mapRowToPaymentHistory(results);
            }
        } catch (Exception e) {
            throw new DaoException("Failed to retrieve payment history", e);
        }
        return null;
    }

    @Override
    public List<PaymentHistory> getAllPaymentsForUser(Long userId) {
        List<PaymentHistory> paymentHistoryList = new ArrayList<>();
        String sql = "SELECT payment_id, user_id, property_id, amount_paid, amount_due, renters_name, late_fee, payment_method, payment_date, due_date " +
                "FROM payment_history WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                paymentHistoryList.add(mapRowToPaymentHistory(results));
            }
        } catch (Exception e) {
            throw new DaoException("Failed to retrieve payment histories for user", e);
        }
        return paymentHistoryList;
    }

    @Override
    public void updatePayment(PaymentHistory paymentHistory) {
        String sql = "UPDATE payment_history SET amount_paid = ?, amount_due = ?, renters_name = ?, late_fee = ?, payment_method = ?, payment_date = ?, due_date = ? " +
                "WHERE payment_id = ?";
        try {
            jdbcTemplate.update(sql,
                    paymentHistory.getAmountPaid(),
                    paymentHistory.getAmountDue(),
                    paymentHistory.getRentersName(),
                    paymentHistory.getLateFee(),
                    paymentHistory.getPaymentMethod(),
                    paymentHistory.getPaymentDate(),
                    paymentHistory.getDueDate(),
                    paymentHistory.getPaymentId());
        } catch (Exception e) {
            throw new DaoException("Failed to update payment history", e);
        }
    }

    @Override
    public void deletePayment(Long userId, Long propertyId) {
        String sql = "DELETE FROM payment_history WHERE user_id = ? AND property_id = ?";
        try {
            jdbcTemplate.update(sql, userId, propertyId);
        } catch (Exception e) {
            throw new DaoException("Failed to delete payment history", e);
        }
    }

    @Override
    public List<PaymentHistory> getAllPaymentsForProperty(Long propertyId) {
        List<PaymentHistory> paymentHistoryList = new ArrayList<>();
        String sql = "SELECT payment_id, user_id, property_id, amount_paid, amount_due, renters_name, late_fee, payment_method, payment_date, due_date " +
                "FROM payment_history WHERE property_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, propertyId);
            while (results.next()) {
                paymentHistoryList.add(mapRowToPaymentHistory(results));
            }
        } catch (Exception e) {
            throw new DaoException("Failed to retrieve payment histories for property", e);
        }
        return paymentHistoryList;
    }

    @Override
    public List<PaymentHistory> getAllPaymentHistories() {
        List<PaymentHistory> paymentHistoryList = new ArrayList<>();
        String sql = "SELECT payment_id, user_id, property_id, amount_paid, amount_due, renters_name, late_fee, payment_method, payment_date, due_date " +
                "FROM payment_history";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                paymentHistoryList.add(mapRowToPaymentHistory(results));
            }
        } catch (Exception e) {
            throw new DaoException("Failed to retrieve all payment histories", e);
        }
        return paymentHistoryList;
    }

    private PaymentHistory mapRowToPaymentHistory(SqlRowSet row) {
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setPaymentId(row.getLong("payment_id"));
        paymentHistory.setUserId(row.getLong("user_id"));
        paymentHistory.setPropertyId(row.getLong("property_id"));
        paymentHistory.setAmountPaid(row.getInt("amount_paid"));
        paymentHistory.setAmountDue(row.getInt("amount_due"));
        paymentHistory.setRentersName(row.getString("renters_name"));
        paymentHistory.setLateFee(row.getInt("late_fee"));
        paymentHistory.setPaymentMethod(row.getString("payment_method"));
        paymentHistory.setPaymentDate(row.getDate("payment_date") != null ? row.getDate("payment_date").toLocalDate() : null);
        paymentHistory.setDueDate(row.getDate("due_date") != null ? row.getDate("due_date").toLocalDate() : null);
        return paymentHistory;
    }
}
