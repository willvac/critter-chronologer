package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import com.udacity.jdnd.course3.critter.user.UserService;
import com.udacity.jdnd.course3.critter.user.entity.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    PetService petService;

    @Autowired
    UserService userService;

    @Autowired
    ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertToSchedule(scheduleDTO);
        return convertToScheduleDTO(scheduleService.saveSchedule(schedule));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return getScheduleDTOList(scheduleService.getAllSchedules());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return getScheduleDTOList(scheduleService.getAllByPetsId(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return getScheduleDTOList(scheduleService.getAllByEmployeeId(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return getScheduleDTOList(scheduleService.getAllByCustomerId(customerId));
    }

    public Schedule convertToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();

        // Copy properties from View to Model
        BeanUtils.copyProperties(scheduleDTO, schedule); // Copies id, date, and activities

        if(scheduleDTO.getPetIds() != null) { //May have pets
            List<Pet> pets = new ArrayList<>();
            for(Long petId : scheduleDTO.getPetIds()){
                if(petId != null) { //Handles null passed in request
                    Pet pet = petService.getPet(petId);
                    if (pet != null) //handles null if pet by petId does not exist
                        pets.add(pet);
                }
            }
            schedule.setPets(pets);
        } //Do not have pets, default is already null


        if(scheduleDTO.getEmployeeIds() != null) { //May have employees
            List<Employee> employees = new ArrayList<>();
            for(Long employeeId : scheduleDTO.getEmployeeIds()) {
                if(employeeId != null) { //Handles null passed in request
                    Employee employee = userService.getEmployee(employeeId);
                    if(employee != null) //handles null if employee by employeeId does not exist
                        employees.add(employee);
                }
            }
            schedule.setEmployees(employees);
        } //else does not have employees, default is already null

        return schedule;
    }

    public ScheduleDTO convertToScheduleDTO(Schedule schedule) {//Should not have null schedules
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        // Copy data from Model to View
        BeanUtils.copyProperties(schedule, scheduleDTO); //Copies id, date, and activities

        if(schedule.getPets() != null) { //May have pets
            List<Long> petIds = new ArrayList<>();
            for(Pet pet : schedule.getPets()) {
                petIds.add(pet.getId());
            }
            scheduleDTO.setPetIds(petIds);
        } //Does not have pets, default is already null

        if(schedule.getEmployees() != null) { //May have employees
            List<Long> employeeIds = new ArrayList<>();
            for(Employee employee : schedule.getEmployees()) {
                employeeIds.add(employee.getId());
            }
            scheduleDTO.setEmployeeIds(employeeIds);
        } //Does not have employees, default is already null

        return scheduleDTO;
    }

    public List<ScheduleDTO> getScheduleDTOList(@NotNull List<Schedule> scheduleList) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule : scheduleList) {
            scheduleDTOS.add(convertToScheduleDTO(schedule));
        }
        return scheduleDTOS;
    }
}
