package com.techelevator.dao;

import com.techelevator.model.MaintenanceRequest;

import java.util.List;

public interface MaintenanceRequestDao {
    List<MaintenanceRequest> getAllRequestsById(Long id);

    MaintenanceRequest getRequestById(Long id);

    MaintenanceRequest submitRequest(MaintenanceRequest maintenanceRequest);

    boolean markRequestAsComplete(Long id);

    boolean assignRequest(Long id, Long workerId);

    List<MaintenanceRequest> getRequestsByWorkerId(Long workerId);

    List<MaintenanceRequest> getAllRequests();
}


