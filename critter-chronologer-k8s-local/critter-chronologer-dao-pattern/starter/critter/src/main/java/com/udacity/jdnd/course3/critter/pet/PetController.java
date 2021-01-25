package com.udacity.jdnd.course3.critter.pet;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
//TODO: Handle return of null vs empty array
/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetDAO petDAO;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        //TODO: Handle case when user spams null everything
        PetDataObject petDataObject = getPetDataObject(petDTO);
        Integer id = petDAO.savePet(petDataObject);
        return getPetDTO(petDAO.getPetById(id));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable Integer petId) {
        PetDataObject petDataObject = petDAO.getPetById(petId);
        return getPetDTO(petDataObject);
    }

    @GetMapping
    public List<PetDTO> getAllPets(){
        List<PetDataObject> petDataObjectList = petDAO.getAllPets();
        return getPetDTOList(petDataObjectList);
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable Integer ownerId) {
        List<PetDataObject> petDataObjectList =  petDAO.getPetsByOwnerId(ownerId);
        return getPetDTOList(petDataObjectList);
    }

    private PetDataObject getPetDataObject(PetDTO petDTO) {
        PetDataObject petDO = new PetDataObject();
        BeanUtils.copyProperties(petDTO, petDO);
        return petDO;
    }

    private PetDTO getPetDTO(PetDataObject petDataObject) {
        if(petDataObject == null)
            return null;

        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(petDataObject, petDTO);
        return petDTO;
    }

    private List<PetDTO> getPetDTOList(List<PetDataObject> petDataObjectList) {
        if(petDataObjectList == null)
            return null;

        List<PetDTO> petDTOList = new ArrayList<>();
        for(PetDataObject petDO : petDataObjectList) {
            PetDTO petDTO = getPetDTO(petDO);
            petDTOList.add(petDTO);
        }

        return petDTOList;
    }
}
