package com.petconomy.backend.repository;

import com.petconomy.backend.model.entity.Household;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {
    boolean deleteHouseholdById(Long id);
    Optional<Household> findHouseholdById(Long id);
    // Find household by exact name match
    Optional<Household> findByName(String name);

    // Find household by name (case insensitive)
    Optional<Household> findByNameIgnoreCase(String name);

    // Find all households containing the name fragment (for search functionality)
    List<Household> findByNameContainingIgnoreCase(String nameFragment);
}
