package com.udacity.jdnd.course3.critter.appointment;

import com.udacity.jdnd.course3.critter.pet.PetDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppointmentDAO {

    @Autowired
    PetDAO petDAO; //TODO: Implement service class instead to make these calls?
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String INSERT_INTO_APPOINTMENTS =
            "INSERT INTO appointments " +
                    "(petid, employeeid, activity, serviceday, servicetime) VALUES " +
                    "(:petId, :employeeId, :activity::petactivity, :serviceDay::dayofweek, :serviceTime)";
    private final String SELECT_ALL_FROM_APPOINTMENTS = "SELECT * FROM appointments";
    private final String SELECT_ALL_FROM_APPOINTMENTS_BY_PETID = "SELECT * FROM appointments WHERE petid = :petId";
    private final String SELECT_ALL_FROM_APPOINTMENTS_BY_EMPLOYEEID = "SELECT * FROM appointments WHERE employeeid = :employeeId";
    private final String SELECT_ALL_FROM_APPOINTMENTS_BY_CUSTOMERID = "SELECT * FROM appointments WHERE petid IN (:petIds)";

    public Integer saveAppointment(AppointmentDataObject appointmentDataObject) {

        BeanPropertySqlParameterSource beanPropertySqlParameterSource =
                new BeanPropertySqlParameterSource(appointmentDataObject);
        beanPropertySqlParameterSource.registerSqlType("activity", 12);
        beanPropertySqlParameterSource.registerSqlType("serviceDay", 12);

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    INSERT_INTO_APPOINTMENTS,
                    beanPropertySqlParameterSource,
                    keyHolder,
                    new String[]{"id"}
            );
            return keyHolder.getKey().intValue();
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }

    }

    public List<AppointmentDataObject> getAllAppointments() {
        try{
            return jdbcTemplate.query(SELECT_ALL_FROM_APPOINTMENTS,
                    new BeanPropertyRowMapper<>(AppointmentDataObject.class));
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public List<AppointmentDataObject> getAllAppointmentsByPetId(Integer petId) {
        try{
            return jdbcTemplate.query(SELECT_ALL_FROM_APPOINTMENTS_BY_PETID,
                    new MapSqlParameterSource("petId", petId),
                    new BeanPropertyRowMapper<>(AppointmentDataObject.class));
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public List<AppointmentDataObject> getAllAppointmentsByCustomerId(Integer customerId) {

        List<Integer> petIds = petDAO.getPetsIdsByOwnerId(customerId);
        //TODO: what to do with null values?
        //TODO: what to do with empty list?

        if(petIds == null || petIds.isEmpty())
            return null;

        try{
            return jdbcTemplate.query(SELECT_ALL_FROM_APPOINTMENTS_BY_CUSTOMERID,
                    new MapSqlParameterSource("petIds", petIds),
                    new BeanPropertyRowMapper<>(AppointmentDataObject.class));
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }

    }

    public List<AppointmentDataObject> getAllAppointmentsByEmployeeId(Integer employeeId) {
        try{
            return jdbcTemplate.query(SELECT_ALL_FROM_APPOINTMENTS_BY_EMPLOYEEID,
                    new MapSqlParameterSource("employeeId", employeeId),
                    new BeanPropertyRowMapper<>(AppointmentDataObject.class));
        }
        catch (EmptyResultDataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }


}
