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

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class EmployeeDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    //SQL for employees table
    private final String SELECT_ALL_FROM_EMPLOYEES_BY_ID = "SELECT * FROM employees WHERE id = :id";
    private final String SELECT_ALL_FROM_EMPLOYEES = "SELECT * FROM employees";
    private final String INSERT_INTO_EMPLOYEES = "INSERT INTO employees (name) VALUES (:name)";
    private final String SELECT_ALL_EMPLOYEES_BY_DAY_AND_SKILLS =
            "SELECT DISTINCT e.id, e.name FROM employees AS e " +
            "JOIN employee_skills AS es ON e.id = es.employeeid " +
            "JOIN employee_availabilities AS ea ON e.id = ea.employeeid " +
            "WHERE dayavailable = :dayAvailable::dayofweek AND skill::varchar IN (:skills)";
    private final String SELECT_ALL_EMPLOYEES_BY_DAY_AND_SKILLS_STRICT =
            "SELECT e.id, e.name FROM employees AS e " +
                    "JOIN employee_availabilities AS ea ON e.id = ea.employeeid " +
                    "WHERE dayavailable = :dayAvailable::dayofweek AND " +
                    "e.id IN " +
                    "(SELECT es.employeeid FROM employee_skills as es " +
                    "WHERE es.skill::varchar IN (:skills) " +
                    "GROUP BY es.employeeid HAVING count(skill) = :count)";
    private final String SELECT_ALL_EMPLOYEES_BY_DAY =
            "SELECT DISTINCT e.id, e.name FROM employees AS e " +
            "JOIN employee_availabilities AS ea ON e.id = ea.employeeid " +
            "WHERE dayavailable = :dayAvailable::dayofweek";
    private final String SELECT_ALL_EMPLOYEES_BY_SKILLS =
            "SELECT DISTINCT e.id, e.name FROM employees as e " +
            "JOIN employee_skills as es on e.id = es.employeeid " +
            "WHERE skill::varchar IN (:skills)";
    private final String SELECT_ALL_EMPLOYEES_BY_SKILLS_STRICT =
            "SELECT e.id, e.name FROM employees as e WHERE e.id IN " +
                    "(SELECT employeeid FROM employee_skills WHERE skill::varchar IN (:skills) " +
                    "GROUP BY employeeid HAVING count(skill) = :count)";
    //SQL for employee_skills table
    private final String SELECT_SKILLS_FROM_EMPLOYEE_SKILLS_BY_EMPLOYEE_ID = "SELECT skill FROM employee_skills WHERE employeeid = :employeeId";
    private final String INSERT_INTO_EMPLOYEE_SKILLS = "INSERT INTO employee_skills (employeeid, skill) VALUES (:employeeId, :skill::petactivity)";
    private final String DELETE_FROM_EMPLOYEE_SKILLS_BY_EMPLOYEEID = "DELETE FROM employee_skills WHERE employeeid = :employeeId";

    //SQL for employee_availabilities
    private final String SELECT_DAYSAVAILABLE_FROM_EMPLOYEE_AVAILABILITIES_BY_EMPLOYEE_ID ="SELECT dayavailable FROM employee_availabilities WHERE employeeid = :employeeId";
    private final String INSERT_INTO_EMPLOYEE_AVAILABILITIES = "INSERT INTO employee_availabilities (employeeid, dayavailable) VALUES (:employeeId, :dayAvailable::dayofweek)";
    private final String DELETE_FROM_EMPLOYEE_AVAILABILITIES_BY_EMPLOYEEID = "DELETE FROM employee_availabilities WHERE employeeid = :employeeId";


    public EmployeeDataObject getEmployeeById(Integer id) {
        try{
            return jdbcTemplate.queryForObject(SELECT_ALL_FROM_EMPLOYEES_BY_ID,
                    new MapSqlParameterSource().addValue("id", id),
                    new BeanPropertyRowMapper<>(EmployeeDataObject.class));
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public List<EmployeeDataObject> getAllEmployees() {
        try{
            return jdbcTemplate.query(SELECT_ALL_FROM_EMPLOYEES,
                    new BeanPropertyRowMapper<>(EmployeeDataObject.class));
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public Integer saveEmployee(EmployeeDataObject employeeDataObject) {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    INSERT_INTO_EMPLOYEES,
                    new BeanPropertySqlParameterSource(employeeDataObject),
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

    public void saveEmployeeSkills(Integer employeeId, Set<EmployeeSkill> skills) {
        try{
            MapSqlParameterSource map = new MapSqlParameterSource().addValue("employeeId", employeeId);
            jdbcTemplate.update(DELETE_FROM_EMPLOYEE_SKILLS_BY_EMPLOYEEID, map);
            map.registerSqlType("skill", 12);

            for(EmployeeSkill skill : skills) {
                map.addValue("skill", skill);
                jdbcTemplate.update(INSERT_INTO_EMPLOYEE_SKILLS, map
                );
            }
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


    public void saveEmployeeAvailabilities(Integer employeeId, Set<DayOfWeek> availabilities) {
        try {
            MapSqlParameterSource map = new MapSqlParameterSource().addValue("employeeId", employeeId);
            jdbcTemplate.update(DELETE_FROM_EMPLOYEE_AVAILABILITIES_BY_EMPLOYEEID, map);
            map.registerSqlType("dayAvailable", 12);

            for(DayOfWeek dayAvailable : availabilities) {
                map.addValue("dayAvailable", dayAvailable);
                jdbcTemplate.update(INSERT_INTO_EMPLOYEE_AVAILABILITIES, map
                );
            }
        } catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public List<EmployeeSkill> getEmployeeSkills(Integer employeeId) {
        try{
            return jdbcTemplate.queryForList(
                    SELECT_SKILLS_FROM_EMPLOYEE_SKILLS_BY_EMPLOYEE_ID,
                    new MapSqlParameterSource().addValue("employeeId", employeeId),
                    EmployeeSkill.class);
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public List<DayOfWeek> getEmployeeAvailabilities(Integer employeeId) {
        try{
            return jdbcTemplate.queryForList(
                    SELECT_DAYSAVAILABLE_FROM_EMPLOYEE_AVAILABILITIES_BY_EMPLOYEE_ID,
                    new MapSqlParameterSource().addValue("employeeId", employeeId),
                    DayOfWeek.class);
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public List<String> getEmployeeSkillSetAsStringList(Set<EmployeeSkill> skills) {
        List<String> skillAsString = new ArrayList<>();
        System.out.println("skills size = " + skills.size());
        for(EmployeeSkill skill : skills) {
            if(skill != null)
                skillAsString.add(skill.toString());
        }
        return skillAsString;
    }

    public List<EmployeeDataObject> getAllEmployeesByDayAndSkills(DayOfWeek dayOfWeek, Set<EmployeeSkill> skills, Boolean strictRequest) {
        try{
            List<String> skillsAsString = getEmployeeSkillSetAsStringList(skills);
            if(strictRequest == null || strictRequest) {
                System.out.println("Inside Strict Request");
                System.out.println("skillAsString inside strict request count: " + skillsAsString.size());
                return jdbcTemplate.query(
                        SELECT_ALL_EMPLOYEES_BY_DAY_AND_SKILLS_STRICT,
                        new MapSqlParameterSource().addValue("dayAvailable", dayOfWeek.toString())
                                .addValue("skills", skillsAsString)
                                .addValue("count", skillsAsString.size()),
                        new BeanPropertyRowMapper<>(EmployeeDataObject.class));
            } else {
                System.out.println("Not inside strict request");
                return jdbcTemplate.query(
                        SELECT_ALL_EMPLOYEES_BY_DAY_AND_SKILLS,
                        new MapSqlParameterSource().addValue("dayAvailable", dayOfWeek.toString())
                                .addValue("skills", skillsAsString),
                        new BeanPropertyRowMapper<>(EmployeeDataObject.class));
            }
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public List<EmployeeDataObject> getAllEmployeesByDay(DayOfWeek dayOfWeek) {
        try{
            return jdbcTemplate.query(
                    SELECT_ALL_EMPLOYEES_BY_DAY,
                    new MapSqlParameterSource().addValue("dayAvailable", dayOfWeek.toString()),
                    new BeanPropertyRowMapper<>(EmployeeDataObject.class));
        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public List<EmployeeDataObject> getAllEmployeesBySkills(Set<EmployeeSkill> skills, Boolean strictRequest) {
        try{
            List<String> skillsAsStringList = getEmployeeSkillSetAsStringList(skills);
            if(strictRequest == null || strictRequest) {
                Integer count = skillsAsStringList.size();
                return jdbcTemplate.query(
                        SELECT_ALL_EMPLOYEES_BY_SKILLS_STRICT,
                        new MapSqlParameterSource().addValue("skills", skillsAsStringList)
                        .addValue("count", count),
                        new BeanPropertyRowMapper<>(EmployeeDataObject.class));
            } else {
                return jdbcTemplate.query(
                        SELECT_ALL_EMPLOYEES_BY_SKILLS,
                        new MapSqlParameterSource().addValue("skills", skillsAsStringList),
                        new BeanPropertyRowMapper<>(EmployeeDataObject.class));
            }

        }
        catch (DataAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

}
