package com.udacity.jdnd.course3.critter.appointment;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import java.sql.Time;
import java.time.DayOfWeek;

public class AppointmentDataObject {
    private Integer id;
    private Integer employeeId;
    private Integer petId;
    private EmployeeSkill activity;
    private DayOfWeek serviceDay;
    private Time serviceTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public EmployeeSkill getActivity() {
        return activity;
    }

    public void setActivity(EmployeeSkill activity) {
        this.activity = activity;
    }

    public DayOfWeek getServiceDay() {
        return serviceDay;
    }

    public void setServiceDay(DayOfWeek serviceDay) {
        this.serviceDay = serviceDay;
    }

    public Time getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Time serviceTime) {
        this.serviceTime = serviceTime;
    }
}
