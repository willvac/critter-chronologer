package com.udacity.jdnd.course3.critter.user.entity;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private long id;

    @Nationalized
    private String name;

    private String phoneNumber;

    @Nationalized
    @Column(length = 500)
    private String notes;

    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Pet> pets;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

}
