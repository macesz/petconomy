package com.petconomy.controller.dto;

import com.petconomy.model.pet.Pet;

public record MyPetDto(String name,
                       String species,
                       String imageUrl,
                       int level,
                       int health,
                       int energy,
                       int petGold) {
    public MyPetDto(Pet pet) {
        this(pet.getName(),
                pet.getTemplate().getSpecies(),
                pet.getTemplate().getImageUrl(),
                pet.getLevel(),
                pet.getHealth(),
                pet.getEnergy(),
                pet.getPetGold());
    }
}
