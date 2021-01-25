package com.udacity.jdnd.course3.critter.user;

import java.util.List;

/**
 * Represents the form that customer request and response data takes. Does not map
 * to the database directly.
 */
public class CustomerDTO {
    private Integer id;
    private String name;
    private String phoneNumber;
    private String notes;
    private List<Integer> petIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Integer> getPetIds() {
        return petIds;
    }

    public void setPetIds(List<Integer> petIds) {
        this.petIds = petIds;
    }
}
