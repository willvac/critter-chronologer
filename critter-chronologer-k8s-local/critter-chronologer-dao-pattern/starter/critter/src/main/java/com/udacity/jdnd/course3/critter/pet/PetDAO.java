package com.udacity.jdnd.course3.critter.pet;

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
public class PetDAO {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_FROM_PETS_BY_ID = "SELECT * FROM pets WHERE id = :id";
    private static final String SELECT_ALL_FROM_PETS = "SELECT * FROM pets";
    private static final String INSERT_INTO_PETS = "INSERT INTO pets " +
            "(name, ownerid, type, birthdate, notes) VALUES " +
            "(:name, :ownerId, :type::pettype, :birthDate, :notes)";
    private static final String SELECT_ALL_FROM_PETS_BY_OWNERID = "SELECT * FROM pets WHERE ownerid = :ownerId";
    private static final String SELECT_ID_FROM_PETS_BY_OWNERID = "SELECT id FROM pets WHERE ownerid = :ownerId";
    private static final String SELECT_OWNERID_FROM_PETS_BY_ID = "SELECT ownerid FROM pets WHERE id = :id";

    public PetDataObject getPetById(Integer id) {
        try{
            return jdbcTemplate.queryForObject(SELECT_ALL_FROM_PETS_BY_ID,
                    new MapSqlParameterSource("id", id),
                    new BeanPropertyRowMapper<>(PetDataObject.class));
        }
        catch (DataAccessException e) {
            return null;
        }
    }

    public Integer savePet(PetDataObject petDataObject) {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            System.out.println(petDataObject.getType());
            BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(petDataObject);
            beanPropertySqlParameterSource.registerSqlType("type", 12);
            jdbcTemplate.update(
                    INSERT_INTO_PETS,
                    beanPropertySqlParameterSource,
                    keyHolder,
                    new String[] {"id"}
            );
            return keyHolder.getKey().intValue();
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            //TODO: Handle exceptions thrown
            return null;
        }
    }

    public List<PetDataObject> getAllPets() {
        try{
            return jdbcTemplate.query(SELECT_ALL_FROM_PETS,
                    new BeanPropertyRowMapper<>(PetDataObject.class));
        }
        catch (DataAccessException e) {
            return null;
        }
    }

    public List<PetDataObject> getPetsByOwnerId(Integer ownerId) {
        try{
            return jdbcTemplate.query(SELECT_ALL_FROM_PETS_BY_OWNERID,
                    new MapSqlParameterSource("ownerId", ownerId),
                    new BeanPropertyRowMapper<>(PetDataObject.class));
        }
        catch (DataAccessException e) {
            return null;
        }
    }

    public List<Integer> getPetsIdsByOwnerId(Integer ownerId) {
        try{
            return jdbcTemplate.queryForList(SELECT_ID_FROM_PETS_BY_OWNERID,
                    new MapSqlParameterSource("ownerId", ownerId),
                    Integer.class);
        }
        catch (DataAccessException e) {
            return null;
        }
    }

    public Integer getOwnerByPetId(Integer petId) {
        try{
            return jdbcTemplate.queryForObject(SELECT_OWNERID_FROM_PETS_BY_ID,
                    new MapSqlParameterSource("id", petId),
                    Integer.class);
        }
        catch (DataAccessException e) {
            return null;
        }
    }
}
