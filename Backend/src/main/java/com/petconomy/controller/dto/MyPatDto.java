package com.petconomy.controller.dto;

import com.petconomy.model.pet.Pet;

public record MyPatDto(String name, int level, int experience, int health, int petGold) {
    public MyPatDto(Pet pet) {
        this(pet.getName(), pet.getLevel(), pet.getExperience(), pet.getHealth(), pet.getPetGold());
    }
}
