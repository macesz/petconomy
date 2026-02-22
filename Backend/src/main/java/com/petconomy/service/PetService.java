package com.petconomy.service;

import com.petconomy.model.pet.Pet;
import com.petconomy.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public void rewardPet(Long memberId, int xpAmount){
        Pet pet = petRepository.findPetByOwnerId(memberId).orElseThrow(() -> new RuntimeException("Pet not found for member with id:"));

        pet.addXp(xpAmount);

        petRepository.save(pet);
    }
}
