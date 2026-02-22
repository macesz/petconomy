package com.petconomy.model.pet;

import com.petconomy.model.user.Member;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int level = 1;
    private int experience = 0;
    private int health = 100;
    private int energy = 100;

    // Internal currency for potions/items, earned through saving
    private int petGold = 0;

    @Enumerated(EnumType.STRING)
    private PetType type;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Member owner;

    public enum PetType {
        CAT, DOG, DRAGON, OWL
    }

    // Example logic for leveling up
    public void addXp(int amount) {
        this.experience += amount;
        if (this.experience >= 100) {
            this.level++;
            this.experience = 0;
        }
    }
}
