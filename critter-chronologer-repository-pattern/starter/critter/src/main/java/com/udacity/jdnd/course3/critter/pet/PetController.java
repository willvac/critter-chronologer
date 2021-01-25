package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.user.UserService;
import com.udacity.jdnd.course3.critter.user.entity.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    UserService userService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = petService.savePet(convertToPet(petDTO));
        return convertToPetDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPet(petId); //May return null if pet by petId does not exist
        return convertToPetDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getAllPets();
        return convertToPetDTOList(pets);

    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.getAllPetsByOwner(ownerId);
        return convertToPetDTOList(pets);
    }

    public Pet convertToPet(PetDTO petDTO) {

        Pet pet = new Pet();

        // Copy properties from View to Model
        BeanUtils.copyProperties(petDTO, pet); //Copies id, type, name, birthDate, and notes
        if(petDTO.getOwnerId() != null) { //Copy owner/customer it not null
            Long customerId = petDTO.getOwnerId();
            Customer customer = userService.getCustomer(customerId); //Return may be null
            pet.setCustomer(customer);
        }//else, default is null

        return pet;
    }

    public PetDTO convertToPetDTO(Pet pet) {

        // Needs to handle null as null may be passed to function
        if(pet == null)
            return null;

        PetDTO petDTO = new PetDTO();

        //Copy properties from Model to View
        BeanUtils.copyProperties(pet, petDTO); //Copies name, id, notes, type, birthDate
        if(pet.getCustomer()!= null) { //Copy customer if not null
            Long ownerId = pet.getCustomer().getId();
            petDTO.setOwnerId(ownerId);
        } // else default = null

        return petDTO;
    }

    public List<PetDTO> convertToPetDTOList(@NotNull List<Pet> pets) {//Handles both empty and non-empty lists
        List<PetDTO> petDTOList = new ArrayList<>();
        for (Pet pet:pets) {
            petDTOList.add(convertToPetDTO(pet));
        }
        return petDTOList;
    }
}
