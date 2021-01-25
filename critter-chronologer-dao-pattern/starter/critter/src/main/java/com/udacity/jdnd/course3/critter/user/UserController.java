package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.PetDAO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    CustomerDAO customerDAO;

    @Autowired
    EmployeeDAO employeeDAO;

    @Autowired
    PetDAO petDAO;
    //TODO: What if a user is created with associated pets? Should we allow this?
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        CustomerDataObject customerDataObject = getCustomerDO(customerDTO);
        Integer id = customerDAO.saveCustomer(customerDataObject);
        if(id != null) {
            customerDataObject.setId(id);
            CustomerDTO returnCustomerDTO = getCustomerDTO(customerDataObject);
            List<Integer> petIds = petDAO.getPetsIdsByOwnerId(id);
            returnCustomerDTO.setPetIds(petIds);
            return returnCustomerDTO;
        }
        return null;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<CustomerDataObject> customerDataObjectList = customerDAO.getAllCustomers();
        return getCustomerDTOList(customerDataObjectList);
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable Integer petId){
        Integer ownerId = petDAO.getOwnerByPetId(petId);
        if(ownerId == null)
            return null;
        CustomerDataObject customerDataObject = customerDAO.getCustomerById(ownerId);
        return getCustomerDTO(customerDataObject);
    }

    public CustomerDataObject getCustomerDO(CustomerDTO customerDTO) {
        CustomerDataObject customerDataObject = new CustomerDataObject();
        BeanUtils.copyProperties(customerDTO, customerDataObject);
        return customerDataObject;
    }

    public CustomerDTO getCustomerDTO(CustomerDataObject customerDataObject) {
        if(customerDataObject == null)
            return null;
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customerDataObject, customerDTO);
        List<Integer> petIds = petDAO.getPetsIdsByOwnerId(customerDTO.getId());
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    public List<CustomerDTO> getCustomerDTOList(List<CustomerDataObject> customerDataObjectList) {
        if(customerDataObjectList == null)
            return null;

        List<CustomerDTO> customerDTOList = new ArrayList<>();
        for(CustomerDataObject customerDataObject : customerDataObjectList) {
            CustomerDTO customerDTO = getCustomerDTO(customerDataObject);
            customerDTOList.add(customerDTO);
        }
        return customerDTOList;
    }



    @PostMapping("/employee")
    @Transactional
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDataObject employeeDataObject = getEmployeeDO(employeeDTO);
        Integer id = employeeDAO.saveEmployee(employeeDataObject);
        if(id != null) {
            employeeDataObject.setId(id);
            if(employeeDTO.getSkills() != null)
            employeeDAO.saveEmployeeSkills(id, employeeDTO.getSkills());
            if(employeeDTO.getDaysAvailable() != null)
            employeeDAO.saveEmployeeAvailabilities(id, employeeDTO.getDaysAvailable());
        }
        return getEmployeeDTO(employeeDataObject);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable Integer employeeId) {
        EmployeeDataObject employeeDataObject = employeeDAO.getEmployeeById(employeeId);
        return getEmployeeDTO(employeeDataObject);
    }

    @GetMapping("/employee")
    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDataObject> employeeDataObjectList = employeeDAO.getAllEmployees();
        return getEmployeeDTOList(employeeDataObjectList);
    }

    @PutMapping("/employee/{employeeId}")
    @Transactional
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable Integer employeeId) {
            employeeDAO.saveEmployeeAvailabilities(employeeId, daysAvailable);
    }

    @PostMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        Set<EmployeeSkill> skills = employeeRequestDTO.getSkills();
        LocalDate date = employeeRequestDTO.getDate();

        DayOfWeek dayOfWeek = null;
        if(date != null)
            dayOfWeek = date.getDayOfWeek();

        List<EmployeeDataObject> results = null;
        Boolean isStrictRequest = employeeRequestDTO.getStrictRequest();

        if(dayOfWeek != null && skills != null) {
            results = employeeDAO.getAllEmployeesByDayAndSkills(dayOfWeek, skills, isStrictRequest);
        } else if (dayOfWeek != null) {
            results = employeeDAO.getAllEmployeesByDay(dayOfWeek);
        } else if (skills != null) {
            results = employeeDAO.getAllEmployeesBySkills(skills, isStrictRequest);
        }
        return getEmployeeDTOList(results);
    }

    public EmployeeDTO getEmployeeDTO(EmployeeDataObject employeeDataObject) {
        if(employeeDataObject == null)
            return null;

        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employeeDataObject, employeeDTO);
        Integer employeeId = employeeDataObject.getId();

        List<EmployeeSkill> skills = employeeDAO.getEmployeeSkills(employeeId);
        if(skills != null && !skills.isEmpty())
            employeeDTO.setSkills(new HashSet<>(skills));
        List<DayOfWeek> daysAvailable = employeeDAO.getEmployeeAvailabilities(employeeId);
        if(daysAvailable != null && !daysAvailable.isEmpty())
            employeeDTO.setDaysAvailable(new HashSet<>(daysAvailable));

        return employeeDTO;

    }

    public EmployeeDataObject getEmployeeDO(EmployeeDTO employeeDTO) {
        EmployeeDataObject employeeDataObject = new EmployeeDataObject();
        BeanUtils.copyProperties(employeeDTO, employeeDataObject);
        return employeeDataObject;
    }

    public List<EmployeeDTO> getEmployeeDTOList(List<EmployeeDataObject> employeeDataObjectList) {
        if(employeeDataObjectList == null)
            return null;

        List<EmployeeDTO> results = new ArrayList<>();
        for(EmployeeDataObject employeeDataObject : employeeDataObjectList) {
            EmployeeDTO employeeDTO = getEmployeeDTO(employeeDataObject);
            results.add(employeeDTO);
        }
        return results;
    }



}
