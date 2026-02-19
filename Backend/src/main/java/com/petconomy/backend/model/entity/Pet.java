package com.petconomy.backend.model.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor

public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int level = 1;
    private int experience = 0;
    private  int health = 100;
    private int energy = 100;
    private int happiness = 100;

    private int petGold = 0;

    @Enumerated(EnumType.STRING)
    private PetType type;

    @OneToOne
    @JoinColumn(name = "user_id")
    UserEntity owner;

    public enum PetType {
        CAT, FOX, BUNNY, OWL
    }

    public Pet(String name, PetType type) {
        this.name = name;
        this.type = type;
    }

    // Example logic for leveling up, need to be improved
    public void addXp(int amount) {
        this.experience += amount;
        if (this.experience >= 100) {
            this.level++;
            this.experience = 0;
        }
    }
}
