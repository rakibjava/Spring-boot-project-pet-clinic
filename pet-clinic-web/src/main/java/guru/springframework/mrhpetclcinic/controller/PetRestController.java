package guru.springframework.mrhpetclcinic.controller;

import guru.springframework.mrhpetclcinic.exception.handling.ExceptionReason;
import guru.springframework.mrhpetclcinic.model.Owner;
import guru.springframework.mrhpetclcinic.model.Pet;
import guru.springframework.mrhpetclcinic.model.PetType;
import guru.springframework.mrhpetclcinic.service.serviceinterface.OwnerService;
import guru.springframework.mrhpetclcinic.service.serviceinterface.PetService;
import guru.springframework.mrhpetclcinic.service.serviceinterface.PetTypeService;
import guru.springframework.mrhpetclcinic.service.serviceinterface.VisitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/owner/")
public class PetRestController {

    public OwnerService ownerService;
    public PetService petService;
    public PetTypeService petTypeService;
    public VisitService visitService;

    public PetRestController(OwnerService ownerService, PetService petService,PetTypeService petTypeService,
                             VisitService visitService) {
        this.ownerService = ownerService;
        this.petService = petService;
        this.petTypeService = petTypeService;
        this.visitService = visitService;
    }
    // http://localhost:7070//owner/pets/{petId}/
    @GetMapping("/pets/{petId}/")
    public ResponseEntity<Pet> getPet(@PathVariable("petId") Long petId) {
        Pet pet = petService.findById(petId);
        if(pet == null)
            throw new ExceptionReason("There is no pet with id:"+ petId );
        else
            return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    // http://localhost:7070//owner/pets/
    @GetMapping("/pets/")
    public ResponseEntity<Set<Pet>> getAllPet() {
        Set<Pet> pet = petService.findAll();
        return new ResponseEntity<>(pet,HttpStatus.OK);
    }

    // http://localhost:7070//owner/{ownerid}/pet/
    @PostMapping("/{ownerid}/pet/")
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet, @PathVariable(name = "ownerid") Long ownerid){
        Owner owner = ownerService.findById(ownerid);
        owner.getPets().add(pet);
        //Pet p= petService.save(pet);
        //ownerService.save(p);
        pet.setOwner(owner);
       // petService.save(pet);
        petTypeService.save(pet.getPetType());
        /*Set <Visit> visit = pet.getVisits();
        for (Visit v: visit){
            v.setPet(pet);
            pet.getVisits().add(v);
        }*/
        return  new ResponseEntity<>(petService.save(pet),HttpStatus.CREATED);
    }
    // http://localhost:7070//owner/{ownerid}/pet/{petid}
    @PutMapping("/{ownerid}/pet/{petid}")
    public ResponseEntity<Owner> updatePet(@RequestBody Pet pet, @PathVariable(name = "ownerid") Long ownerid,
                                           @PathVariable(name = "petid") Long petid){
        Owner owner = ownerService.findById(ownerid);
        Pet petByownerid = petService.findById(petid);
        petByownerid.setName(pet.getName());

        PetType petType = petByownerid.getPetType();//petTypeService.findById(petByownerid.getPetType().getId());
        petType.setName(pet.getPetType().getName());
        //petTypeService.save(petType);

        petByownerid.setPetType(petType);
        petByownerid.setBirthDate(pet.getBirthDate());

        petService.save(petByownerid);

        owner.getPets().add(petByownerid);
        return new ResponseEntity<>(owner,HttpStatus.OK);
    }
    //http://localhost:7070//owner/{ownerid}/deletpet/{petid}/
    @DeleteMapping("/{ownerid}/deletpet/{petId}/")
    public ResponseEntity<Owner> deletePet(@PathVariable(name = "ownerid") Long ownerid,@PathVariable("petId") Long petId) {
       Owner owner = ownerService.findById(ownerid);
       Pet pet = petService.findById(petId);
       //owner.getPets().remove(pet);
       //pet.setPetType(null);

       petService.deleteById(petId);
        //petTypeService.deleteById(petId);
       return new ResponseEntity<>(ownerService.findById(ownerid),HttpStatus.OK);
    }
}
