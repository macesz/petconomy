package com.petconomy.backend.repository;

import com.petconomy.backend.model.entity.Closer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CloserRepository extends JpaRepository<Closer, Long> {
    Closer findFirstByHouseholdIdAndDateLessThanEqualOrderByDateDesc(Long householdId, LocalDate balanceDate);
    List<Closer> findByHouseholdIdOrderByDateDesc(Long householdId);
    Closer findFirstByHouseholdIdOrderByDateDesc(Long householdId);
    Optional<Closer> findByHouseholdIdAndDate(Long householdId, LocalDate date);


}
