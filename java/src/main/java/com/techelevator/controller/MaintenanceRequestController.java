package com.techelevator.controller;

import com.techelevator.dao.MaintenanceRequestDao;
import com.techelevator.model.MaintenanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/maintenance_requests")
public class MaintenanceRequestController {

    @Autowired
    private MaintenanceRequestDao maintenanceRequestDao;

    @GetMapping
    public ResponseEntity<List<MaintenanceRequest>> getAllRequests() {
        List<MaintenanceRequest> requests = maintenanceRequestDao.getAllRequests();
        if (requests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MaintenanceRequest submitRequest(@RequestBody MaintenanceRequest maintenanceRequest) {
        return maintenanceRequestDao.submitRequest(maintenanceRequest);
        //return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRequest> getRequestById(@PathVariable("id") Long id) {
        MaintenanceRequest request = maintenanceRequestDao.getRequestById(id);
        if (request != null) {
            return new ResponseEntity<>(request, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/properties/{id}")
    public ResponseEntity<List<MaintenanceRequest>> getAllRequests(@PathVariable("id") Long id) {
        List<MaintenanceRequest> requests = maintenanceRequestDao.getAllRequestsById(id);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> updateRequestAsComplete(@PathVariable("id") Long id) {
        boolean success = maintenanceRequestDao.markRequestAsComplete(id);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}/assign/{assignedWorkerId}")
    public ResponseEntity<Void> updateRequestAsAssign(@PathVariable("id") Long id, @PathVariable("assignedWorkerId") Long assignedWorkerId) {
        boolean success = maintenanceRequestDao.assignRequest(id, assignedWorkerId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/workers/{workerId}")
    public ResponseEntity<List<MaintenanceRequest>> getRequestsByWorkerId(@PathVariable("workerId") Long workerId) {
        List<MaintenanceRequest> requests = maintenanceRequestDao.getRequestsByWorkerId(workerId);
        if (requests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(requests, HttpStatus.OK);
        }
    }
}