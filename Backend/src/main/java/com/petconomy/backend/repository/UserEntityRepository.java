package com.petconomy.backend.repository;

import com.petconomy.backend.model.entity.Household;
import com.petconomy.backend.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    boolean deleteUserEntityById(Long id);

    Optional<UserEntity> getUserById(Long id);

    Optional<UserEntity> getUserByEmailAndPassword(String email, String password);

    Optional<UserEntity> findUserByName(String username);

    Optional<UserEntity> findUserByEmail(String email);

    List<UserEntity> findAllByHousehold(Household household);

    List<UserEntity> findAllByHouseholdId(Long householdId);

    boolean existsByHouseholdId(Long householdId);

    Long countByHouseholdId(Long householdId);

}
