package com.petconomy.service;
import com.petconomy.model.transaction.Category;
import com.petconomy.model.transaction.Transaction;
import com.petconomy.model.user.Member;
import com.petconomy.repository.MemberRepository;
import com.petconomy.repository.PatRepository;
import com.petconomy.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MonthlyRewardService {

    private final MemberRepository memberRepository;
    private  final PatRepository petRepository;
    private final TransactionRepository transactionRepository;

    public MonthlyRewardService(MemberRepository memberRepository, PatRepository patRepository, TransactionRepository transactionRepository) {
        this.memberRepository = memberRepository;
        this.petRepository = patRepository;
        this.transactionRepository = transactionRepository;
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void rewardMembers(){
        List<Member> users = memberRepository.findAll();

        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        for(Member user : users){
            List<Transaction> recentTransactions = transactionRepository
                    .getAllByMemberAndDateAfter(user, oneMonthAgo)
                    .orElse(List.of());

            int personalBalance = calculatePersonalBalance(recentTransactions);

            if (personalBalance > 0) {
                rewardPet(user);
            }
        }
    }

    private int calculatePersonalBalance(List<Transaction> transactions){
        return transactions.stream()
                .mapToInt(t -> isIncome(t) ? t.getAmount() : -t.getAmount())
                .sum();
    }

    private boolean isIncome(Transaction t){
        return t.getCategory() != null &&
                t.getCategory().getType() == Category.CategoryType.INCOME;    }

    private void rewardPet(Member member){
        petRepository.findPetByOwnerId(member.getId()).ifPresent(pet -> {
            pet.addXp(500);
            pet.setPetGold(pet.getPetGold() + 100);
            petRepository.save(pet);
        });

    }
}
