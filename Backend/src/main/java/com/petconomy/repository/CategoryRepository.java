package com.petconomy.repository;

import com.petconomy.model.transaction.Category;
import com.petconomy.model.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category>findByDefaultValueTrueOrMember(Member member);
    boolean existsByNameAndMember(String name, Member member);
    @Transactional
    void deleteCategoryById(Long id);
}
