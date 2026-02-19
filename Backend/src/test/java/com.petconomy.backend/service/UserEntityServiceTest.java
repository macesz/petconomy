package com.petconomy.backend.service;


import com.petconomy.backend.controller.dto.UpdateProfileDto;
import com.petconomy.backend.controller.dto.UserEntityDto;
import com.petconomy.backend.controller.dto.UserEntityRegistrationDto;
import com.petconomy.backend.controller.exception.HouseholdNotFoundException;
import com.petconomy.backend.controller.exception.UserEntityNotFoundException;
import com.petconomy.backend.model.entity.Household;
import com.petconomy.backend.model.entity.Role;
import com.petconomy.backend.model.entity.Transaction;
import com.petconomy.backend.model.entity.UserEntity;
import com.petconomy.backend.repository.TransactionRepository;
import com.petconomy.backend.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEntityServiceTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private HouseHoldService householdService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserEntityService userEntityService;

    private UserEntityRegistrationDto registrationDto;
    private UpdateProfileDto updateProfileDto;
    private UserEntity userEntity;
    private Household household;
    private Transaction transaction1, transaction2;

    @BeforeEach
    void setUp() {
        registrationDto = new UserEntityRegistrationDto(
                "testUser", "test@gmail.com", "password123"
        );

        updateProfileDto = new UpdateProfileDto(
                "password123",
                "updated@gmail.com",
                "newPassword123",
                "updatedUsername",
                new BigDecimal("1000"),
                "friend@gmail.com"
        );

        household = new Household();
        household.setId(1L);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserName("testUser");
        userEntity.setEmail("test@gmail.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setRoles(Set.of(Role.ROLE_USER));
        userEntity.setTargetAmount(new BigDecimal("0"));
        userEntity.setHousehold(household);

        transaction1 = new Transaction();
        transaction1.setAmount(new BigDecimal("100"));

        transaction2 = new Transaction();
        transaction2.setAmount(new BigDecimal("200"));
    }

    @DisplayName("JUnit test for UserEntityService - register()")
    @Test
    void givenRegistrationDto_whenRegister_thenReturnUserEntityDto() {
        // GIVEN
        given(householdService.createHousehold()).willReturn(household);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(userEntityRepository.save(any(UserEntity.class))).willAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(1L); // Simulate DB saving with ID generation
            return savedUser;
        });

        // WHEN
        UserEntityDto result = userEntityService.register(registrationDto, passwordEncoder);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(registrationDto.name());
        assertThat(result.email()).isEqualTo(registrationDto.email());

        verify(householdService, times(1)).createHousehold();
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userEntityRepository, times(1)).save(any(UserEntity.class));
    }

    @DisplayName("JUnit test for UserEntityService - updateProfile()")
    @Test
    void givenUpdateProfileDto_whenUpdateProfile_thenReturnUpdatedUser() {
        // GIVEN
        UserEntity targetUser = new UserEntity();
        targetUser.setEmail("friend@gmail.com");
        targetUser.setHousehold(new Household());
        targetUser.getHousehold().setId(2L);

        given(userEntityRepository.findUserByEmail("test@gmail.com")).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches("password123", "encodedPassword")).willReturn(true);
        given(userEntityRepository.findUserByEmail("friend@gmail.com")).willReturn(Optional.of(targetUser));
        given(passwordEncoder.encode("newPassword123")).willReturn("newEncodedPassword");
        given(userEntityRepository.save(any(UserEntity.class))).willReturn(userEntity);

        // WHEN
        UserEntityDto result = userEntityService.updateProfile("test@gmail.com", updateProfileDto, passwordEncoder);

        // THEN
        assertThat(result).isNotNull();
        // Add assertions for the expected fields in the result DTO

        // Verify repository interactions
        verify(userEntityRepository, times(1)).findUserByEmail("test@gmail.com");
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
        verify(userEntityRepository, times(1)).findUserByEmail("friend@gmail.com");
        verify(passwordEncoder, times(1)).encode("newPassword123");
        verify(userEntityRepository, times(1)).save(userEntity);
    }


    @DisplayName("JUnit test for UserEntityService - updateProfile() should throw exception for wrong password")
    @Test
    void givenWrongPassword_whenUpdateProfile_thenThrowException() {
        // GIVEN
        given(userEntityRepository.findUserByEmail("test@gmail.com")).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches(updateProfileDto.currentPassword(), "encodedPassword")).willReturn(false);

        // WHEN & THEN
        assertThatThrownBy(() -> userEntityService.updateProfile("test@gmail.com", updateProfileDto, passwordEncoder))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid password");

        // Verify repository interactions
        verify(userEntityRepository, times(1)).findUserByEmail("test@gmail.com");
        verify(passwordEncoder, times(1)).matches(updateProfileDto.currentPassword(), "encodedPassword");
        verify(userEntityRepository, never()).save(any(UserEntity.class));
    }


    @DisplayName("JUnit test for UserEntityService - getUserEntity()")
    @Test
    void givenValidId_whenGetUserEntity_thenReturnUserEntityDto() {
        // GIVEN
        given(userEntityRepository.getUserById(1L)).willReturn(Optional.of(userEntity));

        // WHEN
        UserEntityDto result = userEntityService.getUserEntity(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("testUser");
        assertThat(result.email()).isEqualTo("test@gmail.com");

        // Verify repository interaction
        verify(userEntityRepository, times(1)).getUserById(1L);
    }

    @DisplayName("JUnit test for UserEntityService - getUserEntity() should throw exception if user not found")
    @Test
    void givenInvalidId_whenGetUserEntity_thenThrowException() {
        // GIVEN
        given(userEntityRepository.getUserById(999L)).willReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(UserEntityNotFoundException.class, () -> {
            userEntityService.getUserEntity(999L);
        });

        // Verify repository interaction
        verify(userEntityRepository, times(1)).getUserById(999L);
    }

    @DisplayName("JUnit test for UserEntityService - deleteUserEntity()")
    @Test
    void givenValidId_whenDeleteUserEntity_thenReturnTrue() {
        // GIVEN
        given(userEntityRepository.deleteUserEntityById(1L)).willReturn(true);

        // WHEN
        boolean deleted = userEntityService.deleteUserEntity(1L);

        // THEN
        assertThat(deleted).isTrue();

        // Verify repository interaction
        verify(userEntityRepository, times(1)).deleteUserEntityById(1L);
    }

    @DisplayName("JUnit test for UserEntityService - findMemberByEmail()")
    @Test
    void givenValidEmail_whenFindMemberByEmail_thenReturnUserEntity() {
        // GIVEN
        given(userEntityRepository.findUserByEmail("test@gmail.com")).willReturn(Optional.of(userEntity));

        // WHEN
        UserEntity result = userEntityService.findUserByEmail("test@gmail.com");

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@gmail.com");

        // Verify repository interaction
        verify(userEntityRepository, times(1)).findUserByEmail("test@gmail.com");
    }

    @DisplayName("JUnit test for UserEntityService - getMySaving()")
    @Test
    void givenValidEmail_whenGetMySaving_thenReturnSavingAmount() {
        // GIVEN
        List<Transaction> transactions = List.of(transaction1, transaction2);
        userEntity.setTargetAmount(new BigDecimal("500"));

        given(userEntityRepository.findUserByEmail("test@gmail.com")).willReturn(Optional.of(userEntity));
        given(transactionRepository.getAllByUserEntity(userEntity)).willReturn(Optional.of(transactions));

        // WHEN
        BigDecimal saving = userEntityService.getMySaving("test@gmail.com");

        // THEN
        assertThat(saving).isEqualTo(new BigDecimal("200")); // 500 - (100 + 200)

        // Verify repository interactions
        verify(userEntityRepository, times(1)).findUserByEmail("test@gmail.com");
        verify(transactionRepository, times(1)).getAllByUserEntity(userEntity);
    }

    @DisplayName("JUnit test for UserEntityService - getHouseholdMembers()")
    @Test
    void givenValidHouseholdId_whenGetHouseholdMembers_thenReturnMembersList() {
        // GIVEN
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setUserName("User1");
        user1.setEmail("user1@example.com");

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setUserName("User2");
        user2.setEmail("user2@example.com");

        List<UserEntity> householdMembers = List.of(user1, user2);

        given(householdService.findHouseholdById(1L)).willReturn(Optional.of(household));
        given(userEntityRepository.findAllByHousehold(household)).willReturn(householdMembers);

        // WHEN
        List<UserEntityDto> result = userEntityService.getHouseholdMembers(1L);

        // THEN
        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("User1");
        assertThat(result.get(1).name()).isEqualTo("User2");

        // Verify service interactions
        verify(householdService, times(1)).findHouseholdById(1L);
        verify(userEntityRepository, times(1)).findAllByHousehold(household);
    }

    @DisplayName("JUnit test for UserEntityService - getHouseholdMembers() should throw exception if household not found")
    @Test
    void givenInvalidHouseholdId_whenGetHouseholdMembers_thenThrowException() {
        // GIVEN
        given(householdService.findHouseholdById(999L)).willReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(HouseholdNotFoundException.class, () -> {
            userEntityService.getHouseholdMembers(999L);
        });

        // Verify service interaction
        verify(householdService, times(1)).findHouseholdById(999L);
    }

    @DisplayName("JUnit test for UserEntityService - getCurrentUserHouseholdMembers()")
    @Test
    void givenValidEmail_whenGetCurrentUserHouseholdMembers_thenReturnMembersList() {
        // GIVEN
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setUserName("User1");
        user1.setEmail("user1@example.com");

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setUserName("User2");
        user2.setEmail("user2@example.com");

        List<UserEntity> householdMembers = List.of(user1, user2);

        given(userEntityRepository.findUserByEmail("test@gmail.com")).willReturn(Optional.of(userEntity));
        given(userEntityRepository.findAllByHousehold(household)).willReturn(householdMembers);

        // WHEN
        List<UserEntityDto> result = userEntityService.getCurrentUserHouseholdMembers("test@gmail.com");

        // THEN
        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("User1");
        assertThat(result.get(1).name()).isEqualTo("User2");

        // Verify repository interactions
        verify(userEntityRepository, times(1)).findUserByEmail("test@gmail.com");
        verify(userEntityRepository, times(1)).findAllByHousehold(household);
    }

    @DisplayName("JUnit test for UserEntityService - isHouseholdEmpty()")
    @Test
    void givenHouseholdId_whenIsHouseholdEmpty_thenReturnResult() {
        // GIVEN
        given(userEntityRepository.existsByHouseholdId(1L)).willReturn(true);
        given(userEntityRepository.existsByHouseholdId(2L)).willReturn(false);

        // WHEN
        boolean nonEmptyResult = userEntityService.isHouseholdEmpty(1L);
        boolean emptyResult = userEntityService.isHouseholdEmpty(2L);

        // THEN
        assertThat(nonEmptyResult).isFalse(); // Not empty
        assertThat(emptyResult).isTrue(); // Empty

        // Verify repository interactions
        verify(userEntityRepository, times(1)).existsByHouseholdId(1L);
        verify(userEntityRepository, times(1)).existsByHouseholdId(2L);
    }
}
