package com.petconomy.backend.service;
import com.petconomy.backend.model.entity.Household;
import com.petconomy.backend.repository.HouseholdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HouseHoldServiceTest {

    @Mock
    private HouseholdRepository householdRepository;

    @InjectMocks
    private HouseHoldService houseHoldService;

    private Household household;

    @BeforeEach
    void setUp() {
        household = new Household();
        household.setId(1L);
    }

    @DisplayName("JUnit test for findHouseholdById - household found")
    @Test
    void givenHouseholdId_whenFindHouseholdById_thenReturnHousehold() {
        // GIVEN
        given(householdRepository.findHouseholdById(1L)).willReturn(Optional.of(household));

        // WHEN
        Optional<Household> foundHousehold = houseHoldService.findHouseholdById(1L);

        // THEN
        assertThat(foundHousehold).isPresent();
        assertThat(foundHousehold.get().getId()).isEqualTo(1L);

        // Verify repository interaction
        verify(householdRepository, times(1)).findHouseholdById(1L);
    }

    @DisplayName("JUnit test for findHouseholdById - household not found")
    @Test
    void givenInvalidHouseholdId_whenFindHouseholdById_thenReturnEmptyOptional() {
        // GIVEN
        given(householdRepository.findHouseholdById(99L)).willReturn(Optional.empty());

        // WHEN
        Optional<Household> foundHousehold = houseHoldService.findHouseholdById(99L);

        // THEN
        assertThat(foundHousehold).isEmpty();

        // Verify repository interaction
        verify(householdRepository, times(1)).findHouseholdById(99L);
    }

    @DisplayName("JUnit test for deleteHouseholdById - successful deletion")
    @Test
    void givenHouseholdId_whenDeleteHouseholdById_thenReturnTrue() {
        // GIVEN
        given(householdRepository.deleteHouseholdById(1L)).willReturn(true);

        // WHEN
        boolean result = houseHoldService.deleteHouseholdById(1L);

        // THEN
        assertThat(result).isTrue();

        // Verify repository interaction
        verify(householdRepository, times(1)).deleteHouseholdById(1L);
    }

    @DisplayName("JUnit test for deleteHouseholdById - failed deletion")
    @Test
    void givenInvalidHouseholdId_whenDeleteHouseholdById_thenReturnFalse() {
        // GIVEN
        given(householdRepository.deleteHouseholdById(99L)).willReturn(false);

        // WHEN
        boolean result = houseHoldService.deleteHouseholdById(99L);

        // THEN
        assertThat(result).isFalse();

        // Verify repository interaction
        verify(householdRepository, times(1)).deleteHouseholdById(99L);
    }

    @DisplayName("JUnit test for createHousehold with specified ID")
    @Test
    void givenHouseholdId_whenCreateHousehold_thenReturnCreatedHousehold() {
        // GIVEN
        Household newHousehold = new Household(2L);
        given(householdRepository.save(any(Household.class))).willReturn(newHousehold);

        // WHEN
        Household createdHousehold = houseHoldService.createHousehold(2L);

        // THEN
        assertThat(createdHousehold).isNotNull();
        assertThat(createdHousehold.getId()).isEqualTo(2L);

        // Verify repository interaction
        verify(householdRepository, times(1)).save(any(Household.class));
    }

    @DisplayName("JUnit test for createHousehold without specified ID")
    @Test
    void whenCreateHouseholdWithoutId_thenReturnCreatedHousehold() {
        // GIVEN
        Household newHousehold = new Household();
        newHousehold.setId(3L); // Repository would set the ID after save

        given(householdRepository.save(any(Household.class))).willReturn(newHousehold);

        // WHEN
        Household createdHousehold = houseHoldService.createHousehold();

        // THEN
        assertThat(createdHousehold).isNotNull();
        assertThat(createdHousehold.getId()).isEqualTo(3L);

        // Verify repository interaction
        verify(householdRepository, times(1)).save(any(Household.class));
    }
}
