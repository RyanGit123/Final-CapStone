package com.techelevator.dao;

import com.techelevator.model.Property;

import java.util.List;

public interface PropertyDao {
    Property addProperty(Property property);

    Property getPropertyById(long id);

    List<Property> getAllProperties();

    Property updateProperty(Property property);

    int deleteProperty(Long id);
}
