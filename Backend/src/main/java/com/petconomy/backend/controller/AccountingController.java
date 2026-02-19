package com.petconomy.backend.controller;

import com.petconomy.backend.model.entity.Closer;
import com.petconomy.backend.service.AccountingService;
import com.petconomy.backend.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/accounting")
public class AccountingController {

    private final AccountingService accountingService;
    private final UserEntityService userEntityService;

    @Autowired
    public AccountingController(AccountingService accountingService, UserEntityService userEntityService) {
        this.accountingService = accountingService;
        this.userEntityService = userEntityService;
    }

    @GetMapping("/balance/{householdId}")
    public ResponseEntity<BigDecimal> getHouseholdBalance(
            @PathVariable Long householdId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // If no date provided, use current date
        LocalDate balanceDate = date != null ? date : LocalDate.now();

        BigDecimal balance = accountingService.getBalance(householdId, balanceDate);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/balance/current")
    public ResponseEntity<BigDecimal> getCurrentHouseholdBalance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // Get current user's household
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long householdId = userEntityService.findUserByEmail(email).getHousehold().getId();

        // If no date provided, use current date
        LocalDate balanceDate = date != null ? date : LocalDate.now();

        BigDecimal balance = accountingService.getBalance(householdId, balanceDate);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/closer/{householdId}")
    public ResponseEntity<Closer> createCloser(
            @PathVariable Long householdId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        try {
            Closer closer = accountingService.createCloser(householdId, date);
            return ResponseEntity.status(HttpStatus.CREATED).body(closer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/savings")
    public ResponseEntity<BigDecimal> getCurrentUserSavings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        BigDecimal savings = userEntityService.getMySaving(email);
        return ResponseEntity.ok(savings);
    }
}
