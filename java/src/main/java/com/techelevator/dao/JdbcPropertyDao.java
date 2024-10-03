package com.techelevator.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Property;
//import com.techelevator.dao.PropertyDao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import com.techelevator.model.User;

import javax.sql.DataSource;

@Component
public class JdbcPropertyDao implements PropertyDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcPropertyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Property addProperty(Property property){
        Property newProperty = null;
        String sql = "INSERT INTO public.properties(address_street, address_city,\n" +
                     "address_state, address_zip, description, status, bathroom_count, room_count, price)\n" +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) returning property_id";
        try {
            int propertyId = jdbcTemplate.queryForObject(sql, int.class, property.getStreet(), property.getCity(), property.getState(), property.getZip(), property.getDescription(), property.getStatus(), property.getBathroomCount(), property.getRoomCount(), property.getPrice());
            newProperty = getPropertyById(propertyId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
        return newProperty;
    }
    @Override
    public Property getPropertyById(long id){
        Property property = null;

        String sql ="SELECT property_id, address_street, address_city, address_state, address_zip, description, status, bathroom_count, room_count, price\n" +
                "\tFROM public.properties WHERE property_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if(results.next()){
                property = mapRowToProperty(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return property;
    }
    @Override
    public List<Property> getAllProperties(){
        List<Property> propertyList = new ArrayList();
        String sql = "SELECT property_id, address_street, address_city, address_state, address_zip, description, status, bathroom_count, room_count, price" +
                "\nFROM public.properties;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()){
                Property property = mapRowToProperty(results);
                propertyList.add(property);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return propertyList;
    }
    @Override
    public Property updateProperty(Property property){
        Property  updatedProperty = null;
        String sql = "UPDATE public.properties\n" +
                "\tSET  address_street=?, address_city=?, address_state=?, address_zip=?, description=?, status=?, bathroom_count=?, room_count=?, price=?\n" +
                "\tWHERE property_id=?;";
        try {
            int rowsAffected = jdbcTemplate.update(sql, property.getStreet(), property.getCity(), property.getState(), property.getZip(), property.getDescription(), property.getStatus(), property.getBathroomCount(), property.getRoomCount(), property.getPrice(), property.getId());
            if (rowsAffected != 1) {
                throw new DaoException("Zero or Too many rows affected.");
            } else {
                updatedProperty = getPropertyById(property.getId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
        return updatedProperty;
    }
    @Override
    public int deleteProperty(Long propertyId){
        int numberOfRows = 0;
        String sql = "DELETE FROM public.properties\n" +
                "\tWHERE property_id = ?;";
        try {
            numberOfRows = jdbcTemplate.update(sql, propertyId);
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;



    }

     private Property mapRowToProperty(SqlRowSet row){
           Property property = new Property();
           property.setId(row.getLong("property_id"));
           property.setStreet(row.getString("address_street"));
           property.setCity(row.getString("address_city"));
           property.setState(row.getString("address_state"));
           property.setZip(row.getInt("address_zip"));
           property.setDescription(row.getString("description"));
           property.setStatus(row.getBoolean("status"));
           property.setBathroomCount(row.getDouble("bathroom_count"));
           property.setRoomCount(row.getInt("room_count"));
           property.setPrice(row.getDouble("price"));

       return property;
     }



}
