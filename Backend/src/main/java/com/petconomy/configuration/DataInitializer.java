package com.petconomy.configuration;
import com.petconomy.model.transaction.Category;
import com.petconomy.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
