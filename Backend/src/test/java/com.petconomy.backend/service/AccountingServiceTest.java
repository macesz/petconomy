package com.petconomy.backend.service;

import com.petconomy.backend.model.entity.Closer;
import com.petconomy.backend.model.entity.Household;
import com.petconomy.backend.model.entity.Transaction;
import com.petconomy.backend.repository.CloserRepository;
import com.petconomy.backend.repository.HouseholdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class)
public class AccountingServiceTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private CloserRepository closerRepository;

    @Mock
    private HouseholdRepository householdRepository;

    @InjectMocks
    private AccountingService accountingService;

    private Household household;
    private Closer lastCloser, newerCloser;
    private Transaction transaction1, transaction2;
    private LocalDate balanceDate, closerDate, newerCloserDate;
    private List<Closer> closerList;

    @BeforeEach
    void setUp() {
        // Set up Household
        household = new Household();
        household.setId(1L);

        // Set up dates
        closerDate = LocalDate.of(2023, 1, 1);
        newerCloserDate = LocalDate.of(2023, 2, 1);
        balanceDate = LocalDate.of(2023, 2, 15);

        // Set up Closers
        lastCloser = new Closer();
        lastCloser.setId(1L);
        lastCloser.setHousehold(household);
        lastCloser.setDate(closerDate);
        lastCloser.setAmount(new BigDecimal("1000.00"));

        newerCloser = new Closer();
        newerCloser.setId(2L);
        newerCloser.setHousehold(household);
        newerCloser.setDate(newerCloserDate);
        newerCloser.setAmount(new BigDecimal("1500.00"));

        closerList = Arrays.asList(newerCloser, lastCloser);

        // Set up Transactions
        transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setAmount(new BigDecimal("100.00"));

        transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(new BigDecimal("200.00"));
    }

    @DisplayName("JUnit test for AccountingService - getBalance() with previous closer")
    @Test
    void givenHouseholdIdAndDate_whenGetBalance_thenReturnCorrectBalance() {
        // GIVEN
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        given(closerRepository.findFirstByHouseholdIdAndDateLessThanEqualOrderByDateDesc(
                1L, balanceDate)).willReturn(lastCloser);

        given(transactionService.getTransactionsBetweenDates(
                1L, closerDate, balanceDate)).willReturn(transactions);

        // WHEN
        BigDecimal balance = accountingService.getBalance(1L, balanceDate);

        // THEN
        // Assert balance is correct: 1000 (closer) + 100 + 200 (transactions) = 1300
        assertThat(balance).isEqualTo(new BigDecimal("1300.00"));

        // Verify repository interactions
        verify(closerRepository, times(1))
                .findFirstByHouseholdIdAndDateLessThanEqualOrderByDateDesc(1L, balanceDate);
        verify(transactionService, times(1))
                .getTransactionsBetweenDates(1L, closerDate, balanceDate);
    }

    @DisplayName("JUnit test for AccountingService - getBalance() with no previous closer")
    @Test
    void givenHouseholdIdAndDate_whenGetBalanceWithNoCloser_thenReturnZero() {
        // GIVEN
        given(closerRepository.findFirstByHouseholdIdAndDateLessThanEqualOrderByDateDesc(
                1L, balanceDate)).willReturn(null);

        // WHEN
        BigDecimal balance = accountingService.getBalance(1L, balanceDate);

        // THEN
        assertThat(balance).isEqualTo(BigDecimal.ZERO);
        assertThat(balance).isNotNull();

        // Verify repository interaction
        verify(closerRepository, times(1))
                .findFirstByHouseholdIdAndDateLessThanEqualOrderByDateDesc(1L, balanceDate);
        verify(transactionService, never()).getTransactionsBetweenDates(any(), any(), any());
    }

    @DisplayName("JUnit test for AccountingService - getBalance() with empty transactions")
    @Test
    void givenHouseholdIdAndDate_whenGetBalanceWithNoTransactions_thenReturnCloserAmount() {
        // GIVEN
        given(closerRepository.findFirstByHouseholdIdAndDateLessThanEqualOrderByDateDesc(
                1L, balanceDate)).willReturn(lastCloser);

        given(transactionService.getTransactionsBetweenDates(
                1L, closerDate, balanceDate)).willReturn(Collections.emptyList());

        // WHEN
        BigDecimal balance = accountingService.getBalance(1L, balanceDate);

        // THEN
        // Assert balance equals closer amount with no added transactions
        assertThat(balance).isEqualTo(new BigDecimal("1000.00"));

        // Verify repository interactions
        verify(closerRepository, times(1))
                .findFirstByHouseholdIdAndDateLessThanEqualOrderByDateDesc(1L, balanceDate);
        verify(transactionService, times(1))
                .getTransactionsBetweenDates(1L, closerDate, balanceDate);
    }

    @DisplayName("JUnit test for AccountingService - createCloser()")
    @Test
    void givenHouseholdIdAndDate_whenCreateCloser_thenReturnNewCloser() {
        // GIVEN
        given(householdRepository.findById(1L)).willReturn(Optional.of(household));

        // Mock the dependencies that getBalance() would call internally
        given(closerRepository.findFirstByHouseholdIdAndDateLessThanEqualOrderByDateDesc(
                1L, balanceDate)).willReturn(lastCloser);

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        given(transactionService.getTransactionsBetweenDates(
                1L, closerDate, balanceDate)).willReturn(transactions);

        // Capture the closer object being saved
        ArgumentCaptor<Closer> closerCaptor = ArgumentCaptor.forClass(Closer.class);
        given(closerRepository.save(closerCaptor.capture())).willAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        Closer result = accountingService.createCloser(1L, balanceDate);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(new BigDecimal("1300.00"));
        assertThat(result.getDate()).isEqualTo(balanceDate);
        assertThat(result.getHousehold()).isEqualTo(household);

        // Verify the values being saved
        Closer savedCloser = closerCaptor.getValue();
        assertThat(savedCloser.getHousehold()).isEqualTo(household);
        assertThat(savedCloser.getDate()).isEqualTo(balanceDate);
        assertThat(savedCloser.getAmount()).isEqualTo(new BigDecimal("1300.00"));

        // Verify repository interactions
        verify(householdRepository, times(1)).findById(1L);
        verify(closerRepository, times(1)).save(any(Closer.class));
    }

    @DisplayName("JUnit test for AccountingService - createCloser() with non-existent household")
    @Test
    void givenInvalidHouseholdId_whenCreateCloser_thenThrowException() {
        // GIVEN
        given(householdRepository.findById(999L)).willReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> accountingService.createCloser(999L, balanceDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Household not found");

        // Verify repository interaction
        verify(householdRepository, times(1)).findById(999L);
        verify(closerRepository, never()).save(any(Closer.class));
    }

    @DisplayName("JUnit test for AccountingService - getLatestCloser()")
    @Test
    void givenHouseholdId_whenGetLatestCloser_thenReturnMostRecentCloser() {
        // GIVEN
        given(closerRepository.findFirstByHouseholdIdOrderByDateDesc(1L)).willReturn(newerCloser);

        // WHEN
        Closer result = accountingService.getLatestCloser(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getDate()).isEqualTo(newerCloserDate);
        assertThat(result.getAmount()).isEqualTo(new BigDecimal("1500.00"));

        // Verify repository interaction
        verify(closerRepository, times(1)).findFirstByHouseholdIdOrderByDateDesc(1L);
    }

    @DisplayName("JUnit test for AccountingService - getLatestCloser() with no closers")
    @Test
    void givenHouseholdIdWithNoClosers_whenGetLatestCloser_thenReturnNull() {
        // GIVEN
        given(closerRepository.findFirstByHouseholdIdOrderByDateDesc(1L)).willReturn(null);

        // WHEN
        Closer result = accountingService.getLatestCloser(1L);

        // THEN
        assertThat(result).isNull();

        // Verify repository interaction
        verify(closerRepository, times(1)).findFirstByHouseholdIdOrderByDateDesc(1L);
    }

    @DisplayName("JUnit test for AccountingService - getClosersByHousehold()")
    @Test
    void givenHouseholdId_whenGetClosersByHousehold_thenReturnAllClosersOrdered() {
        // GIVEN
        given(closerRepository.findByHouseholdIdOrderByDateDesc(1L)).willReturn(closerList);

        // WHEN
        List<Closer> result = accountingService.getClosersByHousehold(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L); // Newer closer should be first
        assertThat(result.get(1).getId()).isEqualTo(1L);

        // Verify repository interaction
        verify(closerRepository, times(1)).findByHouseholdIdOrderByDateDesc(1L);
    }

    @DisplayName("JUnit test for AccountingService - getClosersByHousehold() with no closers")
    @Test
    void givenHouseholdIdWithNoClosers_whenGetClosersByHousehold_thenReturnEmptyList() {
        // GIVEN
        given(closerRepository.findByHouseholdIdOrderByDateDesc(1L)).willReturn(Collections.emptyList());

        // WHEN
        List<Closer> result = accountingService.getClosersByHousehold(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        // Verify repository interaction
        verify(closerRepository, times(1)).findByHouseholdIdOrderByDateDesc(1L);
    }

    @DisplayName("JUnit test for AccountingService - getCloserByDate()")
    @Test
    void givenHouseholdIdAndSpecificDate_whenGetCloserByDate_thenReturnMatchingCloser() {
        // GIVEN
        given(closerRepository.findByHouseholdIdAndDate(1L, closerDate)).willReturn(Optional.of(lastCloser));

        // WHEN
        Closer result = accountingService.getCloserByDate(1L, closerDate);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDate()).isEqualTo(closerDate);

        // Verify repository interaction
        verify(closerRepository, times(1)).findByHouseholdIdAndDate(1L, closerDate);
    }

    @DisplayName("JUnit test for AccountingService - getCloserByDate() with no matching date")
    @Test
    void givenHouseholdIdAndNonMatchingDate_whenGetCloserByDate_thenReturnNull() {
        // GIVEN
        LocalDate nonExistingDate = LocalDate.of(2023, 3, 1);
        given(closerRepository.findByHouseholdIdAndDate(1L, nonExistingDate)).willReturn(Optional.empty());

        // WHEN
        Closer result = accountingService.getCloserByDate(1L, nonExistingDate);

        // THEN
        assertThat(result).isNull();

        // Verify repository interaction
        verify(closerRepository, times(1)).findByHouseholdIdAndDate(1L, nonExistingDate);
    }
}
