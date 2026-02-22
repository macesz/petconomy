package com.petconomy.service;

import com.petconomy.controller.dto.CategoryDto;
import com.petconomy.controller.dto.NewTransactionDto;
import com.petconomy.controller.dto.TransactionDto;
import com.petconomy.controller.exception.CategoryNotFoundException;
import com.petconomy.controller.exception.MemberNotFoundException;
import com.petconomy.controller.exception.TransactionNotFoundException;
import com.petconomy.model.transaction.Category;
import com.petconomy.model.user.Member;
import com.petconomy.model.transaction.Transaction;
import com.petconomy.repository.CategoryRepository;
import com.petconomy.repository.MemberRepository;
import com.petconomy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, MemberRepository memberRepository, MemberService memberService, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.categoryRepository = categoryRepository;
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

    public List<TransactionDto> getAllByUser(String email, LocalDate startDate) {

        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        List<Transaction> transactions = new ArrayList<>();
        if(startDate == null){
            transactions = transactionRepository.getAllByMember(member)
                    .orElseThrow(TransactionNotFoundException::new);
        } else {
            transactions = transactionRepository.getAllByMemberAndDateAfter(member, startDate)
                .orElseThrow(TransactionNotFoundException::new);
        }
        return transactions.stream()
                .map(TransactionDto::new)
                .toList();
    }

    public Long createTransaction(String email,NewTransactionDto transactionDto) {
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Category category = categoryRepository.getCategoryById(transactionDto.categoryId())
                .orElseThrow(CategoryNotFoundException::new);
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction(transactionDto);
        transaction.setMember(member);
        transaction.setCategory(category);
        transaction.setDate(date);
        return transactionRepository.save(transaction).getId();
    }

    public List<TransactionDto> getTransactionByGategory(Category category) {

        List<Transaction> transactions = new ArrayList<>();

        transactions = transactionRepository.getTransactionsByCategory(category)
                    .orElseThrow(TransactionNotFoundException::new);

        return transactions.stream()
                .map(TransactionDto::new)
                .toList();
    }

    public TransactionDto getTransactionById(Long id) {
        return transactionRepository.getTransactionById(id)
                .map(transaction -> new TransactionDto(transaction.getId(), transaction.getName(), transaction.getCategory(), transaction.getAmount(), transaction.getMember().getId(), transaction.getDate()
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

    public int getSumOfTransactionByCategoryId(Long categoryId) {
        List<Transaction> transactions = transactionRepository.getAllByCategoryId(categoryId)
                .orElseThrow(TransactionNotFoundException::new);
        return transactions.stream()
                .mapToInt(Transaction::getAmount)
                .sum();
    }

    public OptionalDouble getAvgSpendingByCategoryId(Long categoryId) {
        List<Transaction> transactions = transactionRepository.getAllByCategoryId(categoryId)
                .orElseThrow(TransactionNotFoundException::new);
        return transactions.stream()
                .mapToInt(Transaction::getAmount)
                .average();
    }

}