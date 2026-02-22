package com.petconomy.repository;

import com.petconomy.model.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean deleteMemberById(Long id);

    Optional<Member> getMemberById(Long id);

    Optional<Member> getMemberByEmailAndPassword(String email, String password);

    Optional<Member> findMemberByName(String username);

    Optional<Member> findMemberByEmail(String email);
}
