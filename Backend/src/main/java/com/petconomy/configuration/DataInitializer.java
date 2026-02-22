package com.petconomy.configuration;
import com.petconomy.model.pet.Pet;
import com.petconomy.model.transaction.Category;
import com.petconomy.model.user.Member;
import com.petconomy.repository.CategoryRepository;
import com.petconomy.repository.MemberRepository;
import com.petconomy.repository.PatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;

@Configuration

public class DataInitializer {

    @Bean
    public CommandLineRunner initCategories(CategoryRepository categoryRepository) {
        return args -> {
            // Only populate if empty
            if (categoryRepository.count() == 0) {
                for (Category.CategoryType type : Category.CategoryType.values()) {
                    categoryRepository.save(new Category(type));
                }
                categoryRepository.flush();
            }
        };
    }

    @Bean
    @Order(2)
    public CommandLineRunner initDummyMember(MemberRepository memberRepository, PatRepository patRepository) {
        return args -> {
            if (memberRepository.count() == 0) {
                Member testUser = new Member();
                testUser.setName("DopamineGamer");
                testUser.setEmail("test@petconomy.com");
                testUser.setPassword("pas1234");
                testUser.setTargetAmount(new BigDecimal(1000));// Ensure this matches your security
                memberRepository.save(testUser);

                // Give the test user a starter pet
                Pet starterPet = new Pet();
                starterPet.setName("Penny");
                starterPet.setOwner(testUser);
                starterPet.setExperience(50); // Halfway to level 2!
                starterPet.setPetGold(100);
                patRepository.save(starterPet);
            }
        };
    }

}
