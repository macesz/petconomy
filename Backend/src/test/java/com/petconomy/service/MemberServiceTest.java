package com.petconomy.service;
import com.petconomy.controller.dto.MemberDto;
import com.petconomy.controller.dto.MyPetDto;
import com.petconomy.controller.exception.PetNotFoundException;
import com.petconomy.model.pet.Pet;
import com.petconomy.model.transaction.Transaction;
import com.petconomy.model.user.Member;
import com.petconomy.repository.MemberRepository;
import com.petconomy.repository.PetRepository;
import com.petconomy.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PetRepository petRepository;

    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        memberService = new MemberService(memberRepository, transactionRepository, petRepository);
    }

    @Test
    public void getMySaving_withMultipleTransactions_calculatesCorrectRemainingAmount() {
        String email = "test@petconomy.com";
        Member member = new Member();
        member.setEmail(email);
        member.setTargetAmount(BigDecimal.valueOf(100));

        Transaction t1 = new Transaction();
        t1.setAmount(10);
        Transaction t2 = new Transaction();
        t2.setAmount(30);

        when(memberRepository.findMemberByEmail(email)).thenReturn(Optional.of(member));
        when(transactionRepository.getAllByMember(member)).thenReturn(Optional.of(List.of(t1, t2)));

        // Act
        int result = memberService.getMySaving(email);

        // Assert: 100 - (10 + 30) = 60
        assertEquals(60, result);
    }
    @Test
    public void getMyPet_validEmail_returnsPetDto() {
        // Arrange
        String email = "user@petconomy.com";
        Long memberId = 1L;

        Member member = new Member();
        member.setId(memberId);
        member.setEmail(email);

        Pet pet = new Pet();
        pet.setName("Penny");
        pet.setLevel(1);
        pet.setHealth(100);
        pet.setPetGold(10);
        pet.setOwner(member);

        when(memberRepository.findMemberByEmail(email)).thenReturn(Optional.of(member));
        when(petRepository.findPetByOwnerId(memberId)).thenReturn(Optional.of(pet));

        // Act
        MyPetDto result = memberService.getMyPet(email);

        // Assert
        assertNotNull(result);
        assertEquals("Penny", result.name());
        assertEquals(1, result.level());
        assertEquals(10, result.petGold());
    }

    @Test
    public void getMyPet_memberExistsButPetMissing_throwsPetNotFoundException() {
        // Arrange
        String email = "nopet@test.com";
        Member member = new Member();
        member.setId(2L);

        when(memberRepository.findMemberByEmail(email)).thenReturn(Optional.of(member));
        when(petRepository.findPetByOwnerId(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PetNotFoundException.class, () -> memberService.getMyPet(email));
    }

    @Test
    public void getMember_existingId_returnsMemberDto() {
        // Arrange
        Long memberId = 1L;
        Member member = new Member();
        member.setId(memberId);
        member.setName("Test User");
        member.setEmail("test@test.com");

        when(memberRepository.getMemberById(memberId)).thenReturn(Optional.of(member));

        // Act
        MemberDto result = memberService.getMember(memberId);

        // Assert
        assertEquals(memberId, result.id());
        assertEquals("Test User", result.name());
    }

    @Test
    public void findMemberByEmail_nonExistingEmail_returnsNull() {
        // Arrange
        String email = "missing@test.com";
        when(memberRepository.findMemberByEmail(email)).thenReturn(Optional.empty());

        // Act
        Member result = memberService.findMemberByEmail(email);

        // Assert
        assertNull(result);
    }

}
