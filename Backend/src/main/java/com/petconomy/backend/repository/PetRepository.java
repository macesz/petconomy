package com.petconomy.backend.repository;

import com.petconomy.backend.model.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long>{

    Optional<Pet> findByOwnerId(Long userId);

}
