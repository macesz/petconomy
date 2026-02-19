package com.petconomy.backend.service;
import com.petconomy.backend.controller.dto.CategoryDto;
import com.petconomy.backend.controller.dto.NewTransactionDto;
import com.petconomy.backend.controller.dto.TransactionDto;
import com.petconomy.backend.controller.exception.CategoryNotFoundException;
import com.petconomy.backend.model.entity.Category;
import com.petconomy.backend.model.entity.Household;
import com.petconomy.backend.model.entity.Transaction;
import com.petconomy.backend.model.entity.UserEntity;
import com.petconomy.backend.repository.CategoryRepository;
import com.petconomy.backend.repository.HouseholdRepository;
import com.petconomy.backend.repository.TransactionRepository;
import com.petconomy.backend.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private HouseholdRepository householdRepository;

    @Mock
    private UserEntityService userEntityService;

    @InjectMocks
    private TransactionService transactionService;

    private UserEntity userEntity;
    private Household household;
    private Category foodCategory, entertainmentCategory;
    private Transaction transaction1, transaction2;
    private NewTransactionDto newTransactionDto;
    private TransactionDto transactionDto;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();

        household = new Household();
        household.setId(1L);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserName("testUser");
        userEntity.setEmail("test@example.com");
        userEntity.setHousehold(household);

        // Create categories with CategoryType
        foodCategory = new Category();
        foodCategory.setId(1L);
        foodCategory.setType(Category.CategoryType.GROCERY);
        foodCategory.setDescription(Category.CategoryType.GROCERY.getDescription());
        foodCategory.setColor(Category.CategoryType.GROCERY.getColor());
        foodCategory.setHousehold(household);

        entertainmentCategory = new Category();
        entertainmentCategory.setId(2L);
        entertainmentCategory.setType(Category.CategoryType.ENTERTAINMENT);
        entertainmentCategory.setDescription(Category.CategoryType.ENTERTAINMENT.getDescription());
        entertainmentCategory.setColor(Category.CategoryType.ENTERTAINMENT.getColor());
        entertainmentCategory.setHousehold(household);

        Set<Category> categories = new HashSet<>(Arrays.asList(foodCategory, entertainmentCategory));

        transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setName("Groceries");
        transaction1.setCategories(Set.of(foodCategory));
        transaction1.setAmount(new BigDecimal("50.00"));
        transaction1.setUserEntity(userEntity);
        transaction1.setHousehold(household);
        transaction1.setDate(today);

        transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setName("Movie");
        transaction2.setCategories(Set.of(entertainmentCategory));
        transaction2.setAmount(new BigDecimal("15.00"));
        transaction2.setUserEntity(userEntity);
        transaction2.setHousehold(household);
        transaction2.setDate(today);

        newTransactionDto = new NewTransactionDto(
                "New Transaction",
                List.of(1L, 2L),
                new BigDecimal("25.00")
        );

        // Create a set of CategoryDto objects from the Category objects
        Set<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryDto::new)
                .collect(java.util.stream.Collectors.toSet());

        transactionDto = new TransactionDto(
                1L,
                "Groceries",
                categoryDtos,
                new BigDecimal("50.00"),
                1L,
                1L,
                today
        );
    }

    @DisplayName("JUnit test for getAllCategories method")
    @Test
    void testGetAllCategories() {
        // GIVEN
        List<Category> categories = Arrays.asList(foodCategory, entertainmentCategory);
        given(categoryRepository.findAll()).willReturn(categories);

        // WHEN
        List<CategoryDto> result = transactionService.getAllCategories();

        // THEN
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);


        verify(categoryRepository, times(1)).findAll();
    }

    @DisplayName("JUnit test for getAllTransactions method")
    @Test
    void testGetAllTransactions() {
        // GIVEN
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        given(transactionRepository.findAll()).willReturn(transactions);

        // WHEN
        List<TransactionDto> result = transactionService.getAllTransactions();

        // THEN
        assertThat(result).hasSize(2);

        verify(transactionRepository, times(1)).findAll();
    }

    @DisplayName("JUnit test for getAllByUserEntity with no date filter")
    @Test
    void testGetAllByUserEntityNoDateFilter() {
        // GIVEN
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        given(userRepository.findUserByEmail("test@example.com")).willReturn(Optional.of(userEntity));
        given(transactionRepository.getAllByUserEntity(userEntity)).willReturn(Optional.of(transactions));

        // WHEN
        List<TransactionDto> result = transactionService.getAllByUserEntity("test@example.com", null);

        // THEN
        assertThat(result).hasSize(2);

        verify(userRepository, times(1)).findUserByEmail("test@example.com");
        verify(transactionRepository, times(1)).getAllByUserEntity(userEntity);
    }

    @DisplayName("JUnit test for createTransaction method")
    @Test
    void testCreateTransaction() {
        // GIVEN
        given(userRepository.findUserByEmail("test@example.com")).willReturn(Optional.of(userEntity));
        given(categoryRepository.findAllById(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(foodCategory, entertainmentCategory));
        given(transactionRepository.save(any(Transaction.class))).willAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(3L);
            return savedTransaction;
        });

        // WHEN
        Long transactionId = transactionService.createTransaction("test@example.com", newTransactionDto);

        // THEN
        assertThat(transactionId).isEqualTo(3L);

        verify(userRepository, times(1)).findUserByEmail("test@example.com");
        verify(categoryRepository, times(1)).findAllById(Arrays.asList(1L, 2L));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @DisplayName("JUnit test for createTransaction with no categories")
    @Test
    void testCreateTransactionNoCategoriesFound() {
        // GIVEN
        given(userRepository.findUserByEmail("test@example.com")).willReturn(Optional.of(userEntity));
        given(categoryRepository.findAllById(Arrays.asList(1L, 2L))).willReturn(Collections.emptyList());

        // WHEN/THEN
        assertThrows(CategoryNotFoundException.class, () -> {
            transactionService.createTransaction("test@example.com", newTransactionDto);
        });

        verify(userRepository, times(1)).findUserByEmail("test@example.com");
        verify(categoryRepository, times(1)).findAllById(Arrays.asList(1L, 2L));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @DisplayName("JUnit test for getTransactionsByCategories method")
    @Test
    void testGetTransactionsByCategories() {
        // GIVEN
        Collection<Category> categories = Arrays.asList(foodCategory, entertainmentCategory);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        given(transactionRepository.findByCategoriesIn(categories)).willReturn(Optional.of(transactions));

        // WHEN
        List<TransactionDto> result = transactionService.getTransactionsByCategories(categories);

        // THEN
        assertThat(result).hasSize(2);

        verify(transactionRepository, times(1)).findByCategoriesIn(categories);
    }

    @DisplayName("JUnit test for getTransactionByCategory method")
    @Test
    void testGetTransactionByCategory() {
        // GIVEN
        List<Transaction> transactions = Arrays.asList(transaction1);
        given(transactionRepository.findByCategoriesIn(Collections.singleton(foodCategory))).willReturn(Optional.of(transactions));

        // WHEN
        List<TransactionDto> result = transactionService.getTransactionByCategory(foodCategory);

        // THEN
        assertThat(result).hasSize(1);

        verify(transactionRepository, times(1)).findByCategoriesIn(Collections.singleton(foodCategory));
    }

    @DisplayName("JUnit test for getTransactionsByCategoryId method")
    @Test
    void testGetTransactionsByCategoryId() {
        // GIVEN
        List<Transaction> transactions = Arrays.asList(transaction1);
        given(categoryRepository.existsById(1L)).willReturn(true);
        given(transactionRepository.findByCategories_Id(1L)).willReturn(Optional.of(transactions));

        // WHEN
        List<TransactionDto> result = transactionService.getTransactionsByCategoryId(1L);

        // THEN
        assertThat(result).hasSize(1);

        verify(categoryRepository, times(1)).existsById(1L);
        verify(transactionRepository, times(1)).findByCategories_Id(1L);
    }

    @DisplayName("JUnit test for getTransactionById method")
    @Test
    void testGetTransactionById() {
        // GIVEN
        given(transactionRepository.getTransactionById(1L)).willReturn(Optional.of(transaction1));

        // WHEN
        TransactionDto result = transactionService.getTransactionById(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Groceries");

        verify(transactionRepository, times(1)).getTransactionById(1L);
    }

    @DisplayName("JUnit test for updateTransaction method")
    @Test
    void testUpdateTransaction() {
        // GIVEN
        given(transactionRepository.save(any(Transaction.class))).willReturn(transaction1);

        // WHEN
        boolean result = transactionService.updateTransaction(transactionDto);

        // THEN
        assertThat(result).isTrue();

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @DisplayName("JUnit test for deleteTransaction method")
    @Test
    void testDeleteTransaction() {
        // GIVEN
        given(transactionRepository.deleteTransactionById(1L)).willReturn(true);

        // WHEN
        boolean result = transactionService.deleteTransaction(1L);

        // THEN
        assertThat(result).isTrue();

        verify(transactionRepository, times(1)).deleteTransactionById(1L);
    }

    @DisplayName("JUnit test for getSumOfTransactionByCategoryId method")
    @Test
    void testGetSumOfTransactionByCategoryId() {
        // GIVEN
        List<Transaction> transactions = Arrays.asList(
                transaction1,
                new Transaction() {{
                    setAmount(new BigDecimal("25.00"));
                    setCategories(Set.of(foodCategory));
                }}
        );
        given(transactionRepository.findByCategories_Id(1L)).willReturn(Optional.of(transactions));

        // WHEN
        BigDecimal result = transactionService.getSumOfTransactionByCategoryId(1L);

        // THEN
        assertThat(result).isEqualTo(new BigDecimal("75.00")); // 50 + 25

        verify(transactionRepository, times(1)).findByCategories_Id(1L);
    }

    @DisplayName("JUnit test for getTransactionsBetweenDates method")
    @Test
    void testGetTransactionsBetweenDates() {
        // GIVEN
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        given(householdRepository.findById(1L)).willReturn(Optional.of(household));
        given(transactionRepository.findByHouseholdAndDateBetween(household, startDate, endDate))
                .willReturn(Optional.of(transactions));

        // WHEN
        List<Transaction> result = transactionService.getTransactionsBetweenDates(1L, startDate, endDate);

        // THEN
        assertThat(result).hasSize(2);

        verify(householdRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).findByHouseholdAndDateBetween(household, startDate, endDate);
    }

//    @DisplayName("JUnit test for getAllIncomeByHousholdInDateRanger method")
//    @Test
//    void testGetAllIncomeByHousholdInDateRanger() {
//        // GIVEN
//        LocalDate startDate = today.minusDays(7);
//        LocalDate endDate = today;
//        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
//
//        given(transactionRepository.getAllIncomeByHouseholdByDateRange(household, startDate, endDate))
//                .willReturn(Optional.of(transactions));
//
//        // WHEN
//        List<Transaction> result = transactionService.getAllIncomeByHousholdInDateRanger(household, startDate, endDate);
//
//        // THEN
//        assertThat(result).hasSize(2);
//
//        verify(transactionRepository, times(1)).getAllIncomeByHouseholdByDateRange(household, startDate, endDate);
//    }
}
