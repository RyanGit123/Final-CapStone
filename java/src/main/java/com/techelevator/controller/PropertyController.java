package com.techelevator.controller;

import com.techelevator.dao.PropertyDao;
import com.techelevator.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/properties")
public class  PropertyController {
    @Autowired
    private PropertyDao propertyDao;


    public PropertyController (PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
    }



    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyDao.getAllProperties();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable("id") long id) {
        Property property = propertyDao.getPropertyById(id);
        if (property != null) {
            return new ResponseEntity<>(property, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public  Property addProperty(@RequestBody Property property) {
        return propertyDao.addProperty(property);
       // return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProperty(@PathVariable("id") long id, @RequestBody Property property) {
        Property existingProperty = propertyDao.getPropertyById(id);
        if (existingProperty != null) {
            property.setId(id);
            propertyDao.updateProperty(property);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable("id") long id) {
        Property existingProperty = propertyDao.getPropertyById(id);
        if (existingProperty != null) {
            propertyDao.deleteProperty(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
