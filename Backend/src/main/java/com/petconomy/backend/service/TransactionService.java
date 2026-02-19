package com.petconomy.backend.service;

import com.petconomy.backend.controller.dto.CategoryDto;
import com.petconomy.backend.controller.dto.NewTransactionDto;
import com.petconomy.backend.controller.dto.TransactionDto;
import com.petconomy.backend.controller.exception.CategoryNotFoundException;
import com.petconomy.backend.controller.exception.UserEntityNotFoundException;
import com.petconomy.backend.controller.exception.TransactionNotFoundException;
import com.petconomy.backend.model.entity.Category;
import com.petconomy.backend.model.entity.Household;
import com.petconomy.backend.model.entity.UserEntity;
import com.petconomy.backend.model.entity.Transaction;
import com.petconomy.backend.repository.CategoryRepository;
import com.petconomy.backend.repository.HouseholdRepository;
import com.petconomy.backend.repository.UserEntityRepository;
import com.petconomy.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserEntityRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final HouseholdRepository householdRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserEntityRepository userRepository, CategoryRepository categoryRepository, HouseholdRepository householdRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.householdRepository = householdRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::new)
                .toList();
    }

    public List<TransactionDto> getAllTransactions() {
        List<Transaction> allTransactions = transactionRepository.findAll();
        List<TransactionDto> transactionDtos = new ArrayList<>();
        allTransactions.forEach(transaction -> transactionDtos.add(new TransactionDto(transaction)));
        return transactionDtos;
    }

    public List<TransactionDto> getAllByUserEntity(String email, LocalDate startDate) {

        UserEntity userEntity = userRepository.findUserByEmail(email)
                .orElseThrow(UserEntityNotFoundException::new);
        List<Transaction> transactions;

        if(startDate == null){
            transactions = transactionRepository.getAllByUserEntity(userEntity)
                    .orElseThrow(TransactionNotFoundException::new);
        } else {
            transactions = transactionRepository.getAllByUserEntityAndDateAfter(userEntity, startDate)
                .orElseThrow(TransactionNotFoundException::new);
        }
        return transactions.stream()
                .map(TransactionDto::new)
                .toList();
    }

    public Long createTransaction(String email,NewTransactionDto transactionDto) {
        UserEntity userEntity = userRepository.findUserByEmail(email)
                .orElseThrow(UserEntityNotFoundException::new);

        assert transactionDto.categoryIds() != null;
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(transactionDto.categoryIds()));

        if (categories.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction(transactionDto);
        transaction.setUserEntity(userEntity); // Changed from setMember to setUserEntity
        transaction.setCategories(categories);
        transaction.setDate(date);
        transaction.setHousehold(userEntity.getHousehold()); // Set house from user

        return transactionRepository.save(transaction).getId();
    }

    public List<TransactionDto> getTransactionsByCategories(Collection<Category> categories) {
        List<Transaction> transactions = transactionRepository.findByCategoriesIn(categories)
                .orElseThrow(TransactionNotFoundException::new);

        return transactions.stream()
                .map(TransactionDto::new)
                .toList();
    }

    // Overloaded method for single category
    public List<TransactionDto> getTransactionByCategory(Category category) {
        return getTransactionsByCategories(Collections.singleton(category));
    }

    public List<TransactionDto> getTransactionsByCategoryId(Long categoryId) {
        // Optional: Check if the category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException();
        }

        // Use the new repository method
        return transactionRepository.findByCategories_Id(categoryId)
                .orElse(List.of())  // Return empty list if no transactions found
                .stream()
                .map(TransactionDto::new)  // Using the constructor that takes Transaction
                .collect(Collectors.toList());
    }

    public TransactionDto getTransactionById(Long id) {
        return transactionRepository.getTransactionById(id)
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getName(),
                        transaction.getCategories().stream()  // Convert Category to CategoryDto
                                .map(CategoryDto::new)
                                .collect(Collectors.toSet()),
                        transaction.getAmount(),
                        transaction.getUserEntity().getId(),
                        transaction.getHousehold() != null ? transaction.getHousehold().getId() : null,  // Add the missing houseId parameter
                        transaction.getDate()
                )).orElseThrow(NoSuchElementException::new);
    }

    public boolean updateTransaction(TransactionDto transactionDto) {
        Transaction transaction = new Transaction(transactionDto);
        transactionRepository.save(transaction);
        return true;
    }

    public boolean deleteTransaction(Long id) {
        return transactionRepository.deleteTransactionById(id);
    }

    public BigDecimal getSumOfTransactionByCategoryId(Long categoryId) {
        List<Transaction> transactions = transactionRepository.findByCategories_Id(categoryId)
                .orElseThrow(TransactionNotFoundException::new);
        return transactions.stream()
                .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);


    }

    public BigDecimal getAvgSpendingByCategoryId(Long categoryId) {
        List<Transaction> transactions = transactionRepository.findByCategories_Id(categoryId)
                .orElseThrow(TransactionNotFoundException::new);
        return transactions.stream()
                .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public List<Transaction> getTransactionsBetweenDates(Long householdId, LocalDate startDate, LocalDate endDate) {
        // Find the household
        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));

        // Get all transactions for the household between the dates
        return transactionRepository.findByHouseholdAndDateBetween(household, startDate, endDate)
                .orElse(List.of());
    }

}