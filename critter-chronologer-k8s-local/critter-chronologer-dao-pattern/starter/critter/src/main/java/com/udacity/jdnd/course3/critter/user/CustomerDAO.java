package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDAO {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public final String SELECT_ALL_FROM_CUSTOMERS = "SELECT * FROM customers";
    public final String SELECT_ALL_FROM_CUSTOMERS_BY_ID = "SELECT * FROM customers WHERE id = :id";
    public final String INSERT_INTO_CUSTOMERS ="INSERT INTO customers " +
            "(name, phonenumber, notes) VALUES " +
            "(:name, :phoneNumber, :notes)";

    public Integer saveCustomer(CustomerDataObject customerDataObject) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    INSERT_INTO_CUSTOMERS,
                    new BeanPropertySqlParameterSource(customerDataObject),
                    keyHolder,
                    new String[] {"id"}
            );
            return keyHolder.getKey().intValue();
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public List<CustomerDataObject> getAllCustomers() {
        try{
            return jdbcTemplate.query(SELECT_ALL_FROM_CUSTOMERS,
                    new BeanPropertyRowMapper<>(CustomerDataObject.class));
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public CustomerDataObject getCustomerById(Integer id) {
        try{
            return jdbcTemplate.queryForObject(SELECT_ALL_FROM_CUSTOMERS_BY_ID,
                    new MapSqlParameterSource("id", id),
                    new BeanPropertyRowMapper<>(CustomerDataObject.class));
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

}
