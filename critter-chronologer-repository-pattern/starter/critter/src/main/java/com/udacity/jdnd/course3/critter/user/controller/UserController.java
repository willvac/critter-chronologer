package com.udacity.jdnd.course3.critter.user.controller;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.user.UserService;
import com.udacity.jdnd.course3.critter.user.entity.Customer;
import com.udacity.jdnd.course3.critter.user.entity.Employee;
import com.udacity.jdnd.course3.critter.user.view.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.view.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.view.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.view.EmployeeSkill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//TODO: Handle null date when scheduling/finding availablilit


/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    PetService petService;

    @Autowired
    UserService userService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = userService.saveCustomer(convertToCustomer(customerDTO));
        return convertToCustomerDTO(customer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = userService.getAllCustomers();
        return getCustomerDTOList(customers);
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Customer customer = userService.getCustomerByPetId(petId);
        return convertToCustomerDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertToEmployee(employeeDTO);
        return convertToEmployeeDTO(userService.saveEmployee(employee));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = userService.getEmployee(employeeId);
        return convertToEmployeeDTO(employee);
    }


    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        userService.setAvailability(daysAvailable, employeeId);
    }


    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
            Set<EmployeeSkill> skills = employeeRequestDTO.getSkills();
            LocalDate date = employeeRequestDTO.getDate();
            return getEmployeeDTOList(userService.findEmployeesForService(skills, date));
    }

    public Customer convertToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        // Copy properties from View to Model
        BeanUtils.copyProperties(customerDTO, customer); //Copies id, name, phoneNumber, and notes
        if(customerDTO.getPetIds() != null) { //May have pets
            List<Pet> pets = new ArrayList<>();
            for(Long petId : customerDTO.getPetIds()) {
                Pet pet = petService.getPet(petId);
                if(pet != null) //Pet may not be valid
                    pets.add(pet);
            }
            customer.setPets(pets);
        }//Does not have pets, default is already null

        return customer;
    }

    public CustomerDTO convertToCustomerDTO(Customer customer) {
        //May be null
        if(customer == null)
            return null;

        CustomerDTO customerDTO = new CustomerDTO();
        //Copy properties from Model to View
        BeanUtils.copyProperties(customer, customerDTO); // Copies name, notes, number, id
        if(customer.getPets() != null) { //May have pets
            List<Long> petId = new ArrayList<>();
            for(Pet pet : customer.getPets()) {
                petId.add(pet.getId());
            }
            customerDTO.setPetIds(petId);
        }//Does not have pets, default is already null

        return customerDTO;
    }

    public EmployeeDTO convertToEmployeeDTO(Employee employee) {
        //May be null
        if (employee == null)
            return null;

        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO); // Copy properties from model to view
        return employeeDTO;
    }

    public Employee convertToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee); //Copy properties from View to Model
        return employee;
    }

    public List<CustomerDTO> getCustomerDTOList(List<Customer> customers) {
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        //Convert each customer to customer DTO
        for(Customer customer : customers) {
            customerDTOS.add(convertToCustomerDTO(customer));
        }
        return customerDTOS;
    }

    public List<EmployeeDTO> getEmployeeDTOList(List<Employee> employees) {
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        //Convert each employee to employeeDTO
        for(Employee employee: employees) {
            employeeDTOS.add(convertToEmployeeDTO(employee));
        }
        return employeeDTOS;
    }

}
