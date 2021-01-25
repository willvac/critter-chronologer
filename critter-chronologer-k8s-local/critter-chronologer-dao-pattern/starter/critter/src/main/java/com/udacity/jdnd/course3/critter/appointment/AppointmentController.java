package com.udacity.jdnd.course3.critter.appointment;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    AppointmentDAO appointmentDAO;

    //TODO: How to handle all null? How to handle null values at time and date? Does it make sense to have appointment without those?
    @PostMapping
    public AppointmentDTO createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDataObject appointmentDataObject = getAppointmentDO(appointmentDTO);
        Integer id = appointmentDAO.saveAppointment(appointmentDataObject);
        if(id != null) {
            appointmentDataObject.setId(id);
            return getAppointmentDTO(appointmentDataObject);
        }
        return null;
    }

    @GetMapping
    public List<AppointmentDTO> getAllAppointments() {
        List<AppointmentDataObject> appointmentDataObjectList = appointmentDAO.getAllAppointments();
        return getAppointmentDTOList(appointmentDataObjectList);
    }

    @GetMapping("/pet/{petId}")
    public List<AppointmentDTO> getAppointmentForPet(@PathVariable Integer petId) {
        List<AppointmentDataObject> appointmentDataObjectList = appointmentDAO.getAllAppointmentsByPetId(petId);
        return getAppointmentDTOList(appointmentDataObjectList);
    }

    @GetMapping("/employee/{employeeId}")
    public List<AppointmentDTO> getAppointmentForEmployee(@PathVariable Integer employeeId) {
        List<AppointmentDataObject> appointmentDataObjectList = appointmentDAO.getAllAppointmentsByEmployeeId(employeeId);
        return getAppointmentDTOList(appointmentDataObjectList);
    }

    @GetMapping("/customer/{customerId}")
    public List<AppointmentDTO> getAppointmentForCustomer(@PathVariable Integer customerId) {
        List<AppointmentDataObject> appointmentDataObjectList = appointmentDAO.getAllAppointmentsByCustomerId(customerId);
        return getAppointmentDTOList(appointmentDataObjectList);
    }

    public AppointmentDataObject getAppointmentDO(AppointmentDTO appointmentDTO) {
        AppointmentDataObject appointmentDataObject = new AppointmentDataObject();
        BeanUtils.copyProperties(appointmentDTO, appointmentDataObject);
        return appointmentDataObject;
    }

    public AppointmentDTO getAppointmentDTO(AppointmentDataObject appointmentDataObject) {
        if(appointmentDataObject == null)
            return null;

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        BeanUtils.copyProperties(appointmentDataObject, appointmentDTO);
        return appointmentDTO;
    }

    public List<AppointmentDTO> getAppointmentDTOList(List<AppointmentDataObject> appointmentDataObjectList) {
        if(appointmentDataObjectList == null)
            return null;

        List<AppointmentDTO> results = new ArrayList<>();

        for(AppointmentDataObject appointmentDataObject : appointmentDataObjectList) {
            AppointmentDTO appointmentDTO = getAppointmentDTO(appointmentDataObject);
            results.add(appointmentDTO);
        }
        return results;
    }
}
