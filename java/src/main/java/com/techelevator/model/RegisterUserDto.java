package com.techelevator.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
/*
    The acronym DTO is being used for "data transfer object". It means that this type of class is specifically
    created to transfer data between the client and the server. For example, CredentialsDto represents the data a client must
    pass to the server for a login endpoint, and TokenDto represents the object that's returned from the server
    to the client from a login endpoint.
 */
public class RegisterUserDto {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    @Email(message = "Valid Email")
    private String email;

    @NotEmpty(message = "Full Name")
    private String name;

    private int propertyId = 0;
    private int apartmentId = 0;
    @NotEmpty(message = "Please select a role for this user.")
    private String role;

    public RegisterUserDto() { }

    public RegisterUserDto(String username, String password, String role, String email, String name, int propertyId, int apartmentId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.name = name;
        this.propertyId = propertyId;
        this.apartmentId = apartmentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
