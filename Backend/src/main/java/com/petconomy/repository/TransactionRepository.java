package com.petconomy.repository;

import com.petconomy.model.transaction.Category;
import com.petconomy.model.user.Member;
import com.petconomy.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<List<Transaction>> getTransactionsByCategory(Category category);

    Optional<Transaction> getTransactionById(Long id);

    boolean deleteTransactionById(Long id);

    Optional<List<Transaction>> getAllByCategoryId(Long category_id);

    Optional<List<Transaction>> getAllByMemberAndDateAfter(Member member, LocalDate startDate);

    Optional<List<Transaction>> getAllByMember(Member member);
}
