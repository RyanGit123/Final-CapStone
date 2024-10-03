package com.techelevator.dao;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Property;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.model.MaintenanceRequest;

@Component
public class JdbcMaintenanceRequestDao implements MaintenanceRequestDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcMaintenanceRequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MaintenanceRequest> getAllRequests() {
        List<MaintenanceRequest> requestList = new ArrayList<>();
        String sql = "SELECT id, property_id, description, completed, request_date, due_date, completion_date, assigned_worker_id FROM maintenance_requests";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                MaintenanceRequest request = mapRowToRequest(results);
                requestList.add(request);
            }
        } catch (Exception e) {

            throw new DaoException("Failed to retrieve maintenance requests", e);
        }

        return requestList;
    }

    @Override
    public List<MaintenanceRequest> getAllRequestsById(Long id) {
        List<MaintenanceRequest> requestList = new ArrayList();
        String sql = "SELECT id, property_id, description, completed, request_date, due_date, completion_date, assigned_worker_id FROM maintenance_requests WHERE property_id = ? ORDER BY due_date";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        try {
            while (results.next()) {
                MaintenanceRequest request = mapRowToRequest(results);
                requestList.add(request);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }

        return requestList;
    }


    @Override
    public MaintenanceRequest getRequestById(Long id) {
        String sql = "SELECT id, property_id, description, completed, request_date, due_date, completion_date, assigned_worker_id FROM maintenance_requests WHERE id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        try {
            if (results.next()) {
                return mapRowToRequest(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
        return null;
    }


    @Override
    public MaintenanceRequest submitRequest(MaintenanceRequest maintenanceRequest) {
        MaintenanceRequest newRequest = null;
        String sql = "INSERT INTO maintenance_requests (property_id, description, request_date, due_date, completion_date, completed, assigned_worker_id) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
    
        try {
            // Unwrap Optional<LocalDate> and pass null if completionDate is not present
            LocalDate completionDate = maintenanceRequest.getCompletionDate() != null ? 
                                       maintenanceRequest.getCompletionDate().orElse(null) : null;
    
          
                               
            // Execute the query 
            Long id = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                maintenanceRequest.getPropertyId(),
                maintenanceRequest.getDescription(),
                maintenanceRequest.getRequestDate(),
                maintenanceRequest.getDueDate(),
                completionDate,  // Can be null if Optional is empty
                maintenanceRequest.isCompleted(),
                maintenanceRequest.getAssignedWorkerId()
            );
    
            maintenanceRequest.setId(id);
            newRequest = getRequestById(id);
            return newRequest;
            
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Request could not be submitted", e);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database", e);
        }
    }
    


    @Override
    public boolean markRequestAsComplete(Long id) {
        String sql = "UPDATE maintenance_requests SET completed = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return true;
    }

    @Override
    public boolean assignRequest(Long id, Long workerId) {
        String sql = "UPDATE maintenance_requests SET assigned_worker_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, workerId, id);
        return true;
        }

    @Override
    public List<MaintenanceRequest> getRequestsByWorkerId(Long workerId) {
        String sql = "SELECT id, property_id, description, completed, request_date, due_date, completion_date, assigned_worker_id " +
                "FROM maintenance_requests WHERE assigned_worker_id = ?";

        List<MaintenanceRequest> requests = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, workerId);
            while (results.next()) {
                requests.add(mapRowToRequest(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
        return requests;
    }



private MaintenanceRequest mapRowToRequest(SqlRowSet row) {
    MaintenanceRequest request = new MaintenanceRequest();
    request.setId(row.getLong("id"));
    request.setPropertyId(row.getLong("property_id"));
    request.setDescription(row.getString("description"));
    request.setCompleted(row.getBoolean("completed"));
    request.setRequestDate(row.getDate("request_date").toLocalDate());
    request.setDueDate(row.getDate("due_date").toLocalDate());
    
    // Check if completion_date is null before setting it
    if (row.getDate("completion_date") != null) {
        request.setCompletionDate(Optional.of(row.getDate("completion_date").toLocalDate()));
    } else {
        request.setCompletionDate(Optional.empty()); // No completion date
    }
    
    request.setAssignedWorkerId(row.getLong("assigned_worker_id"));
    return request;
}

}
