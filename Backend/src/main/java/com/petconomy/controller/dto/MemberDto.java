package com.petconomy.controller.dto;

import com.petconomy.model.user.Member;
import com.petconomy.model.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

public record MemberDto(Long id, String name, String email, BigDecimal target, List<Transaction> transactions) {
    public MemberDto(Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getTargetAmount(), member.getTransactions());
    }
}
