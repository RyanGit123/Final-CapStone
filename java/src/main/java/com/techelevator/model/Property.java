package com.techelevator.model;

import java.util.Objects;

public class Property {
    private Long id;
    private String description;
    private boolean status;
    private String street;
    private String state;
    private String city;
    private int zip;
    private double bathroomCount;
    private int roomCount;
    private double price;

    public Property () {}

    public Property(Long id, String description, boolean status, String street, String state, String city, int zip, double bathroomCount, int roomCount, double price) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.street = street;
        this.state = state;
        this.city = city;
        this.zip = zip;
        this.bathroomCount = bathroomCount;
        this.roomCount = roomCount;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public double getBathroomCount() {
        return bathroomCount;
    }

    public void setBathroomCount(double bathroomCount) {
        this.bathroomCount = bathroomCount;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFullAddress() {
        return street + ", " + city + ", " + state + " " + zip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return Objects.equals(id, property.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
