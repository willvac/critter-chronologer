package com.udacity.jdnd.course3.critter.pet.service;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;


    @Transactional
    public Pet savePet(Pet pet) {

        //save pet to db
        Pet petRef = petRepository.save(pet);

        //update owner db with pet
        if (petRef.getCustomer() != null) { //has an owner
            if(petRef.getCustomer().getPets()!= null) { //some pets has been set on owner
                if (!petRef.getCustomer().getPets().contains(pet)) {
                    petRef.getCustomer().getPets().add(pet);
                }
            } else { //no pets has been set on owner
                List<Pet> pets = new ArrayList<>();
                pets.add(pet);
                petRef.getCustomer().setPets(pets);
            }
        }
        //transactional should automatically update db to reflect any changes made to entities

        return petRef;
    }

    public Pet getPet(Long id) {
        //returns null if entity by id does not exist. Other options: throw EntityNotFoundException to explicitly notify users that the entity does not exist?
        return petRepository.findById(id).orElse(null);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getAllPetsByOwner(Long id) {
        return petRepository.findAllByCustomerId(id);
    }
}
