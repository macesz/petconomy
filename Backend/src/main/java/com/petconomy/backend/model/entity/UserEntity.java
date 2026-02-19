package com.petconomy.backend.model.entity;

import com.petconomy.backend.controller.dto.UserEntityDto;
import com.petconomy.backend.controller.dto.UserEntityRegistrationDto;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@SequenceGenerator(name="seq", initialValue=2, allocationSize=100)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq")
    private Long id;
    private String userName;
    private String FirstName;
    private String LastName;
    @Column(unique = true)
    private String email;
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(nullable = false)
    private BigDecimal targetAmount;

    @OneToMany(mappedBy = "userEntity")
    private Set<Transaction> transactions;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private Household household;

    public UserEntity() {
    }

    public UserEntity(String name) {
        this.userName = name;
    }

    public UserEntity(UserEntityRegistrationDto userEntityRegistrationDto) {
        userName = userEntityRegistrationDto.userName();
        email = userEntityRegistrationDto.email();
        password = userEntityRegistrationDto.password();
    }

    public UserEntity(UserEntityDto userEntityDto) {
        id = userEntityDto.id();
        userName = userEntityDto.name();
        email = userEntityDto.email();
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + userName + '\'' +
                ", email='" + email + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity member = (UserEntity) o;
        return id.equals(member.id)
                && Objects.equals(userName, member.userName)
                && Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, email);
    }

}



