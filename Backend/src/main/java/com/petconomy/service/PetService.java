package com.petconomy.service;

import com.petconomy.model.pet.Pet;
import com.petconomy.repository.PatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    @Autowired
    private PatRepository patRepository;

    public void rewardPet(Long memberId, int xpAmount){
        Pet pet = patRepository.findPetByOwnerId(memberId).orElseThrow(() -> new RuntimeException("Pet not found for member with id:"));

        pet.addXp(xpAmount);

        patRepository.save(pet);
    }
}
