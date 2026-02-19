package com.petconomy.backend.service;

import com.petconomy.backend.model.entity.Household;
import com.petconomy.backend.repository.HouseholdRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HouseHoldService {

    private final HouseholdRepository householdRepository;

    public HouseHoldService(HouseholdRepository householdRepository) {

        this.householdRepository = householdRepository;
    }

    public Optional<Household> findHouseholdById(Long id){
        return householdRepository.findHouseholdById(id);
    }

    public boolean deleteHouseholdById(Long id){
        return householdRepository.deleteHouseholdById(id);
    }

    public Household createHousehold(Long householdId) {
        return householdRepository.save(new Household(householdId));
    }
    public Household createHousehold(String defaultName) {
        Household household = new Household();
        household.setHouseName(defaultName);
        return householdRepository.save(household);
    }

    public Optional<Household> findHouseholdByName(String houseName){
        return householdRepository.findByNameIgnoreCase(houseName);
    }

    public List<Household> searchHouseholdsByName(String searchTerm) {
        return householdRepository.findByNameContainingIgnoreCase(searchTerm);
    }

    public List<Household> getAllHouseholds() {
        return householdRepository.findAll();
    }

    public Optional<Household> updateHouseholdName(Long id, String newName) {
        return householdRepository.findHouseholdById(id)
                .map(household -> {
                    household.setHouseName(newName);
                    return householdRepository.save(household);
                });
    }
}
