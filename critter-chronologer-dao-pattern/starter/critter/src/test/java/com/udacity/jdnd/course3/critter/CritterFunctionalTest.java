package com.udacity.jdnd.course3.critter;

import com.google.common.collect.Sets;
import com.udacity.jdnd.course3.critter.appointment.AppointmentController;
import com.udacity.jdnd.course3.critter.appointment.AppointmentDTO;
import com.udacity.jdnd.course3.critter.pet.PetController;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.user.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a set of functional tests to validate the basic capabilities desired for this application.
 * Students will need to configure the application to run these tests by adding application.properties file
 * to the test/resources directory that specifies the datasource. It can run using an in-memory H2 instance
 * and should not try to re-use the same datasource used by the rest of the app.
 *
 * These tests should all pass once the project is complete.
 */

@Transactional
@SpringBootTest(classes = CritterApplication.class)
public class CritterFunctionalTest {

    @Autowired
    private UserController userController;

    @Autowired
    private PetController petController;

    @Autowired
    private AppointmentController appointmentController;

    @Test
    public void testCreateCustomer(){
        CustomerDTO customerDTO = createCustomerDTO();
        CustomerDTO newCustomer = userController.saveCustomer(customerDTO);
        CustomerDTO retrievedCustomer = userController.getAllCustomers().get(0);
        Assertions.assertEquals(newCustomer.getName(), customerDTO.getName());
        Assertions.assertEquals(newCustomer.getId(), retrievedCustomer.getId());
        Assertions.assertTrue(retrievedCustomer.getId() > 0);
    }

    @Test
    public void testCreateEmployee(){
        EmployeeDTO employeeDTO = createEmployeeDTO();
        EmployeeDTO newEmployee = userController.saveEmployee(employeeDTO);
        EmployeeDTO retrievedEmployee = userController.getEmployee(newEmployee.getId());
        Assertions.assertEquals(employeeDTO.getSkills(), newEmployee.getSkills());
        Assertions.assertEquals(newEmployee.getId(), retrievedEmployee.getId());
        Assertions.assertTrue(retrievedEmployee.getId() > 0);
    }

    @Test
    public void testAddPetsToCustomer() {
        CustomerDTO customerDTO = createCustomerDTO();
        CustomerDTO newCustomer = userController.saveCustomer(customerDTO);

        PetDTO petDTO = createPetDTO();
        petDTO.setOwnerId(newCustomer.getId());
        PetDTO newPet = petController.savePet(petDTO);

        //make sure pet contains customer id
        PetDTO retrievedPet = petController.getPet(newPet.getId());
        Assertions.assertEquals(retrievedPet.getId(), newPet.getId());
        Assertions.assertEquals(retrievedPet.getOwnerId(), newCustomer.getId());

        //make sure you can retrieve pets by owner
        List<PetDTO> pets = petController.getPetsByOwner(newCustomer.getId());
        Assertions.assertEquals(newPet.getId(), pets.get(0).getId());
        Assertions.assertEquals(newPet.getName(), pets.get(0).getName());

        //check to make sure customer now also contains pet
        CustomerDTO retrievedCustomer = userController.getAllCustomers().get(0);
        Assertions.assertTrue(retrievedCustomer.getPetIds() != null && retrievedCustomer.getPetIds().size() > 0);
        Assertions.assertEquals(retrievedCustomer.getPetIds().get(0), retrievedPet.getId());
    }

    @Test
    public void testFindPetsByOwner() {
        CustomerDTO customerDTO = createCustomerDTO();
        CustomerDTO newCustomer = userController.saveCustomer(customerDTO);

        PetDTO petDTO = createPetDTO();
        petDTO.setOwnerId(newCustomer.getId());
        PetDTO newPet = petController.savePet(petDTO);
        petDTO.setType(PetType.DOG);
        petDTO.setName("DogName");
        PetDTO newPet2 = petController.savePet(petDTO);

        List<PetDTO> pets = petController.getPetsByOwner(newCustomer.getId());
        Assertions.assertEquals(pets.size(), 2);
        Assertions.assertEquals(pets.get(0).getOwnerId(), newCustomer.getId());
        Assertions.assertEquals(pets.get(0).getId(), newPet.getId());
    }

    @Test
    public void testFindOwnerByPet() {
        CustomerDTO customerDTO = createCustomerDTO();
        CustomerDTO newCustomer = userController.saveCustomer(customerDTO);

        PetDTO petDTO = createPetDTO();
        petDTO.setOwnerId(newCustomer.getId());
        PetDTO newPet = petController.savePet(petDTO);

        CustomerDTO owner = userController.getOwnerByPet(newPet.getId());
        Assertions.assertEquals(owner.getId(), newCustomer.getId());
        Assertions.assertEquals(owner.getPetIds().get(0), newPet.getId());
    }

    @Test
    public void testChangeEmployeeAvailability() {
        EmployeeDTO employeeDTO = createEmployeeDTO();
        EmployeeDTO emp1 = userController.saveEmployee(employeeDTO);
        Assertions.assertNull(emp1.getDaysAvailable());

        Set<DayOfWeek> availability = Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);
        userController.setAvailability(availability, emp1.getId());

        EmployeeDTO emp2 = userController.getEmployee(emp1.getId());
        Assertions.assertEquals(availability, emp2.getDaysAvailable());
    }

    @Test
    public void testFindEmployeesByServiceAndTime() {
        EmployeeDTO emp1 = createEmployeeDTO();
        EmployeeDTO emp2 = createEmployeeDTO();
        EmployeeDTO emp3 = createEmployeeDTO();

        emp1.setDaysAvailable(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
        emp2.setDaysAvailable(Sets.newHashSet(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
        emp3.setDaysAvailable(Sets.newHashSet(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

        emp1.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
        emp2.setSkills(Sets.newHashSet(EmployeeSkill.PETTING, EmployeeSkill.WALKING));
        emp3.setSkills(Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.SHAVING));

        EmployeeDTO emp1n = userController.saveEmployee(emp1);
        EmployeeDTO emp2n = userController.saveEmployee(emp2);
        EmployeeDTO emp3n = userController.saveEmployee(emp3);

        //make a request that matches employee 1 or 2
        EmployeeRequestDTO er1 = new EmployeeRequestDTO();
        er1.setDate(LocalDate.of(2019, 12, 25)); //wednesday
        er1.setSkills(Sets.newHashSet(EmployeeSkill.PETTING));

        Set<Integer> eIds1 = userController.findEmployeesForService(er1).stream().map(EmployeeDTO::getId).collect(Collectors.toSet());
        Set<Integer> eIds1expected = Sets.newHashSet(emp1n.getId(), emp2n.getId());
        Assertions.assertEquals(eIds1, eIds1expected);

        //make a request that matches only employee 3
        EmployeeRequestDTO er2 = new EmployeeRequestDTO();
        er2.setDate(LocalDate.of(2019, 12, 27)); //friday
        er2.setSkills(Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.SHAVING));

        Set<Integer> eIds2 = userController.findEmployeesForService(er2).stream().map(EmployeeDTO::getId).collect(Collectors.toSet());
        Set<Integer> eIds2expected = Sets.newHashSet(emp3n.getId());

        Assertions.assertEquals(eIds2, eIds2expected);
    }

    @Test
    public void testScheduleAppointmentWithEmployee() {
        EmployeeDTO employeeTemp = createEmployeeDTO();
        employeeTemp.setDaysAvailable(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
        EmployeeDTO employeeDTO = userController.saveEmployee(employeeTemp);
        CustomerDTO customerDTO = userController.saveCustomer(createCustomerDTO());
        PetDTO petTemp = createPetDTO();
        petTemp.setOwnerId(customerDTO.getId());
        PetDTO petDTO = petController.savePet(petTemp);

        appointmentController.createAppointment(getAppointmentDTO(petDTO.getId(), employeeDTO.getId(),DayOfWeek.MONDAY, EmployeeSkill.PETTING, new Time(8, 0, 0)));
        AppointmentDTO appointmentDTO = appointmentController.getAllAppointments().get(0);

        Assertions.assertEquals(appointmentDTO.getActivity(), EmployeeSkill.PETTING);
        Assertions.assertEquals(appointmentDTO.getServiceDay(), DayOfWeek.MONDAY);
        Assertions.assertEquals(appointmentDTO.getEmployeeId(), employeeDTO.getId());
        Assertions.assertEquals(appointmentDTO.getPetId(), petDTO.getId());
        Assertions.assertEquals(appointmentDTO.getServiceTime(), new Time(8,0,0));
    }

    @Test
    public void testFindAppointmentByEntities() {

        //Create appointment 1 along with its dependencies (an employee 1, a pet 1, a customer 1)
        CustomerDTO cust1 = userController.saveCustomer(createCustomerDTO());
        PetDTO pet1 = createPetDTO();
        pet1.setOwnerId(cust1.getId());
        pet1 = petController.savePet(pet1);
        EmployeeDTO emp1 = userController.saveEmployee(createEmployeeDTO());
        AppointmentDTO appt1 = createAppointment(pet1.getId(), emp1.getId(),DayOfWeek.MONDAY, EmployeeSkill.PETTING, new Time(8,0,0));

        //Create appointment 2 along with its dependencies (an employee 2, a pet 2, a customer 2)
        CustomerDTO cust2 = userController.saveCustomer(createCustomerDTO());
        PetDTO pet2 = createPetDTO();
        pet2.setOwnerId(cust2.getId());
        pet2 = petController.savePet(pet2);
        EmployeeDTO emp2 = userController.saveEmployee(createEmployeeDTO());
        AppointmentDTO appt2 = createAppointment(pet2.getId(), emp2.getId(),DayOfWeek.WEDNESDAY, EmployeeSkill.SHAVING, new Time(16,0,0));

        //Create appointment 3 along with its dependencies (an employee 1, a pet 2, a customer 2
        AppointmentDTO appt3 = createAppointment(pet2.getId(), emp1.getId(),DayOfWeek.THURSDAY, EmployeeSkill.MEDICATING, new Time(12,30,0));


        /*
            We now have 3 schedule entries. The third schedule entry has the same employees as the 1st schedule
            and the same pets/owners as the second schedule. So if we look up schedule entries for the employee from
            schedule 1, we should get both the first and third schedule as our result.
         */

        //Employee 1 in is both appointment 1 and 3
        List<AppointmentDTO> appt1e = appointmentController.getAppointmentForEmployee(emp1.getId());
        Assertions.assertEquals(2, appt1e.size());
        compareAppointments(appt1, appt1e.get(0));
        compareAppointments(appt3, appt1e.get(1));

        //Employee 2 is only in appointment 2
        List<AppointmentDTO> appt2e = appointmentController.getAppointmentForEmployee(emp2.getId());
        Assertions.assertEquals(1, appt2e.size());
        compareAppointments(appt2, appt2e.get(0));

        //Pet 1 is in appointment 1
        List<AppointmentDTO> appt1p = appointmentController.getAppointmentForPet(pet1.getId());
        Assertions.assertEquals(1, appt1p.size());
        compareAppointments(appt1, appt1p.get(0));

        //Pet from appointment 2 is also in b 3
        List<AppointmentDTO> appt2p = appointmentController.getAppointmentForPet(pet2.getId());
        Assertions.assertEquals(2, appt2p.size());
        compareAppointments(appt2, appt2p.get(0));
        compareAppointments(appt3, appt2p.get(1));

        //Owner of the first pet will only be in appointment 1
        List<AppointmentDTO> appt1c = appointmentController.getAppointmentForCustomer(cust1.getId());
        Assertions.assertEquals(1, appt1c.size());
        compareAppointments(appt1, appt1c.get(0));

        //Owner of pet from appointment 2 will be in both appointments 2 and 3
        List<AppointmentDTO> appt2c = appointmentController.getAppointmentForCustomer(cust2.getId());
        Assertions.assertEquals(2, appt2c.size());
        compareAppointments(appt2, appt2c.get(0));
        compareAppointments(appt3, appt2c.get(1));
    }


    private static EmployeeDTO createEmployeeDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("TestEmployee");
        employeeDTO.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
        return employeeDTO;
    }
    private static CustomerDTO createCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("TestEmployee");
        customerDTO.setPhoneNumber("123-456-789");
        return customerDTO;
    }

    private static PetDTO createPetDTO() {
        PetDTO petDTO = new PetDTO();
        petDTO.setName("TestPet");
        petDTO.setType(PetType.CAT);
        return petDTO;
    }

    private static EmployeeRequestDTO createEmployeeRequestDTO() {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setDate(LocalDate.of(2019, 12, 25));
        employeeRequestDTO.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.WALKING));
        return employeeRequestDTO;
    }


    private static AppointmentDTO getAppointmentDTO(Integer petId, Integer employeeId, DayOfWeek dayOfWeek, EmployeeSkill activity, Time time) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPetId(petId);
        appointmentDTO.setEmployeeId(employeeId);
        appointmentDTO.setServiceDay(dayOfWeek);
        appointmentDTO.setActivity(activity);
        appointmentDTO.setServiceTime(time);
        return appointmentDTO;
    }

    private AppointmentDTO createAppointment(Integer petId, Integer employeeId, DayOfWeek dayOfWeek, EmployeeSkill activity, Time time) {
        return appointmentController.createAppointment(getAppointmentDTO(petId, employeeId, dayOfWeek, activity, time));
    }




    private static void compareAppointments(AppointmentDTO appt1, AppointmentDTO appt2) {
        Assertions.assertEquals(appt1.getId(), appt2.getId());
        Assertions.assertEquals(appt1.getPetId(), appt2.getPetId());
        Assertions.assertEquals(appt1.getActivity(), appt2.getActivity());
        Assertions.assertEquals(appt1.getEmployeeId(), appt2.getEmployeeId());
        Assertions.assertEquals(appt1.getServiceDay(), appt2.getServiceDay());
        Assertions.assertEquals(appt1.getServiceTime(), appt2.getServiceTime());
    }

}
