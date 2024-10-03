package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.RentalAgreement;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
public class JdbcRentalAgreementDao implements RentalAgreementDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcRentalAgreementDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public RentalAgreement addRentalAgreement(RentalAgreement rentalAgreement) {
        String sql = "INSERT INTO rental_agreements (renter_id, landlord_id, property_id, lease_start_date, lease_end_date, rent_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?) returning agreement_id ";
        Long newId = jdbcTemplate.queryForObject(sql, Long.class, rentalAgreement.getRenterId(), rentalAgreement.getLandlordId(),
                rentalAgreement.getPropertyId(), rentalAgreement.getLeaseStartDate(), rentalAgreement.getLeaseEndDate(),
                rentalAgreement.getRentAmount());
        RentalAgreement newRentalAgreement = getRentalAgreementById(newId);
        return newRentalAgreement;
    }

    @Override
    public RentalAgreement getRentalAgreementById(Long agreementId) {
        String sql = "SELECT * FROM rental_agreements WHERE agreement_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{agreementId}, new RentalAgreementRowMapper());
        } catch (Exception e) {
            throw new DaoException("Failed to retrieve rental agreement", e);
        }
    }

    @Override
    public List<RentalAgreement> getAllRentalAgreements() {
        String sql = "SELECT * FROM rental_agreements";
        try {
            return jdbcTemplate.query(sql, new RentalAgreementRowMapper());
        } catch (Exception e) {
            throw new DaoException("Failed to retrieve all rental agreements", e);
        }
    }

    //SS restored from backup
    @Override
    public RentalAgreement updateRentalAgreement(RentalAgreement rentalAgreement) {
        RentalAgreement updatedRentalAgreement = null;
        String sql = "UPDATE rental_agreements SET renter_id = ?, landlord_id = ?, property_id = ?, lease_start_date = ?, lease_end_date = ?, rent_amount = ? WHERE agreement_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, rentalAgreement.getRenterId(), rentalAgreement.getLandlordId(),
                    rentalAgreement.getPropertyId(), rentalAgreement.getLeaseStartDate(), rentalAgreement.getLeaseEndDate(),
                    rentalAgreement.getRentAmount(), rentalAgreement.getAgreementId());
            if (rowsAffected != 1) {
                throw new DaoException("Zero or Too many rows affected.");
            } else {
                updatedRentalAgreement = getRentalAgreementById(rentalAgreement.getAgreementId());
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
        return updatedRentalAgreement;
    }

    @Override
    public int deleteRentalAgreement(Long agreementId) {
        String sql = "DELETE FROM rental_agreements WHERE agreement_id = ?";
        return jdbcTemplate.update(sql, agreementId);
    }

    @Override
    public List<RentalAgreement> getRentalAgreementsByRenterId(Long renterId) {
        String sql = "SELECT * FROM rental_agreements WHERE renter_id = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{renterId}, new RentalAgreementRowMapper());
        } catch (Exception e) {
            throw new DaoException("Failed to retrieve rental agreements for renter", e);
        }
    }

    @Override
    public List<RentalAgreement> getRentalAgreementsByLandlordId(Long landlordId) {
        String sql = "SELECT * FROM rental_agreements WHERE landlord_id = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{landlordId}, new RentalAgreementRowMapper());
        } catch (Exception e) {
            throw new DaoException("Failed to retrieve rental agreements for landlord", e);
        }
    }

    private static final class RentalAgreementRowMapper implements RowMapper<RentalAgreement> {
        @Override
        public RentalAgreement mapRow(ResultSet rs, int rowNum) throws SQLException {
            RentalAgreement rentalAgreement = new RentalAgreement();
            rentalAgreement.setAgreementId(rs.getLong("agreement_id"));
            rentalAgreement.setRenterId(rs.getLong("renter_id"));
            rentalAgreement.setLandlordId(rs.getLong("landlord_id"));
            rentalAgreement.setPropertyId(rs.getLong("property_id"));  // New field
            rentalAgreement.setLeaseStartDate(rs.getTimestamp("lease_start_date").toLocalDateTime());
            rentalAgreement.setLeaseEndDate(rs.getTimestamp("lease_end_date").toLocalDateTime());
            rentalAgreement.setRentAmount(rs.getInt("rent_amount"));
            return rentalAgreement;
        }
    }
}


