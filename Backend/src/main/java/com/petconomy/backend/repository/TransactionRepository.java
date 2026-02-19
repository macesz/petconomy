package com.petconomy.backend.repository;

import com.petconomy.backend.model.entity.Category;
import com.petconomy.backend.model.entity.Household;
import com.petconomy.backend.model.entity.UserEntity;
import com.petconomy.backend.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<List<Transaction>> findByCategories_Id(Long categoryId);

    Optional<Transaction> getTransactionById(Long id);

    Optional<List<Transaction>> findByCategoriesIn(Collection<Category> categories);

    boolean deleteTransactionById(Long id);

    Optional<List<Transaction>> getAllByUserEntityAndDateAfter(UserEntity userEntity, LocalDate startDate);

    Optional<List<Transaction>> getAllByUserEntity(UserEntity userEntity);

    Optional<List<Transaction>> getAllIncomeByHouseholdByDateRange(Household household, LocalDate startDate, LocalDate endDate);

    Optional<List<Transaction>> findByHouseholdAndDateBetween(
            Household household, LocalDate startDate, LocalDate endDate);


}
