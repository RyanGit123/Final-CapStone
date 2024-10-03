package com.techelevator.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class MaintenanceRequest {
    private Long id;
    private Long propertyId;
    private String description;
    private boolean completed;
    private LocalDate requestDate;
    private LocalDate dueDate;
    private Optional<LocalDate> completionDate; // Updated to Optional
    private Long assignedWorkerId;

    public MaintenanceRequest() {}

    public MaintenanceRequest(Long id, Long propertyId, String description, boolean completed, LocalDate requestDate, LocalDate dueDate, Optional<LocalDate> completionDate, Long assignedWorkerId) {
        this.id = id;
        this.propertyId = propertyId;
        this.description = description;
        this.completed = completed;
        this.requestDate = requestDate;
        this.dueDate = dueDate;
        this.completionDate = completionDate;
        this.assignedWorkerId = assignedWorkerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Optional<LocalDate> getCompletionDate() { // Changed return type to Optional
        return completionDate;
    }

    public void setCompletionDate(Optional<LocalDate> completionDate) { // Changed parameter type to Optional
        this.completionDate = completionDate;
    }

    public Long getAssignedWorkerId() {
        return assignedWorkerId;
    }

    public void setAssignedWorkerId(Long assignedWorkerId) {
        this.assignedWorkerId = assignedWorkerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaintenanceRequest that = (MaintenanceRequest) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
