package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.user.entity.Customer;
import com.udacity.jdnd.course3.critter.user.entity.Employee;
import com.udacity.jdnd.course3.critter.user.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.view.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.el.PropertyNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);//if not found, return null instead of throwing an exception
    }

    @Transactional
    public Customer saveCustomer(Customer customer) {
        Customer customerRef = customerRepository.save(customer);
        // if customer has pet, then update customer's pet's owner to customer
        if(customer.getPets() != null) {
            for(Pet pet : customer.getPets()) {
                pet.setCustomer(customer);
            }
        }
        return customerRef;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByPetId(Long petId) {
        return customerRepository.findByPetsId(petId).orElse(null); //if not found, will return null instead of throwing an exception
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElse(null); //if not found, will return null instead of throwing an exception
    }

    @Transactional
    public void setAvailability(Set<DayOfWeek> daysAvailable, Long id) {
        Employee employee = employeeRepository.getOne(id);
        if(employee != null)
            employee.setDaysAvailable(daysAvailable); //Logic: How to handle null? Null currently sets employee's daysAvailable to null
    }

    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, LocalDate date) {

        if(skills == null) {
            throw new PropertyNotFoundException("To find employees for service you must provide the skills/service that you need.");
        }

        if(date == null) {
            throw new PropertyNotFoundException("To find employees for service you must provide a service date.");
        }

        List<Employee> employees = employeeRepository.findAllDistinctByDaysAvailable(date.getDayOfWeek());

        return employees.stream()
                .filter(e -> e.getSkills().containsAll(skills))
                .collect(Collectors.toList());
    }
}
