package com.udacity.jdnd.course3.critter.user.repository;

import com.udacity.jdnd.course3.critter.user.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllDistinctByDaysAvailable(DayOfWeek dayOfWeek);

}
