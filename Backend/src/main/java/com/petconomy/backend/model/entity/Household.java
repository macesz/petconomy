package com.petconomy.backend.model.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Household {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String houseName;

    @OneToMany(mappedBy = "household")
    private Set<UserEntity> users;

    @OneToMany(mappedBy = "household")
    private Set<Category> categories;

    @OneToMany(mappedBy = "household")
    private Set<Transaction> transactions;

    public Household() {};

    public Household(Long id) {
        this.id = id;
    }

    public Household(UserEntity creator) {
        this.houseName = creator.getUserName();
        this.users.add(creator);
        creator.setHousehold(this);
    }

}
