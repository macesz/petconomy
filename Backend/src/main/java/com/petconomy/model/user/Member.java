package com.petconomy.model.user;

import com.petconomy.controller.dto.MemberDto;
import com.petconomy.controller.dto.MemberRegistrationDto;
import com.petconomy.model.pet.Pet;
import com.petconomy.model.transaction.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@SequenceGenerator(name="seq", initialValue=2, allocationSize=100)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq")
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(nullable = false)
    private BigDecimal targetAmount;

    @OneToMany(mappedBy = "member")
    private List<Transaction> transactions;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Pet pet;

    public Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    public Member(MemberRegistrationDto memberRegistrationDto) {
        name = memberRegistrationDto.name();
        email = memberRegistrationDto.email();
        password = memberRegistrationDto.password();
    }



    public Member(MemberDto userDto) {
        id = userDto.id();
        name = userDto.name();
        email = userDto.email();
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id)
                && Objects.equals(name, member.name)
                && Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

}



