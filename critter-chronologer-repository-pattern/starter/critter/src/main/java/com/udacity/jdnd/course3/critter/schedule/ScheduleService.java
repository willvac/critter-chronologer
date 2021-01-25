package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import com.udacity.jdnd.course3.critter.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    public Schedule saveSchedule(Schedule schedule) {
        //TODO: future todo? perform validation to ensure that the employees are actually available on the selected date and that they have the skills
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getAllByPetsId(Long petId) {
        return scheduleRepository.findAllByPetsId(petId);
    }

    public List<Schedule> getAllByEmployeeId(Long employeeId) {
        return scheduleRepository.findAllByEmployeesId(employeeId);
    }

    public List<Schedule> getAllByCustomerId(Long customerId) {
        return scheduleRepository.findAllDistinctByPetsCustomerId(customerId);
    }

}
