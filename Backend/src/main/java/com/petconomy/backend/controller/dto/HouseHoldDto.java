package com.petconomy.backend.controller.dto;

import com.petconomy.backend.model.entity.Household;

public record HouseHoldDto(Long id) {
    public HouseHoldDto(Household household){
        this(household.getId());
    }
}
