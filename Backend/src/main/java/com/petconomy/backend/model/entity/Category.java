package com.petconomy.backend.model.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Data
public class Category {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CategoryType type;

    @ManyToOne
    @JoinColumn(name = "household_id")
    private Household household;

    private String description;

    private String color;

    public Category() {
    }
    public Category(CategoryType type) {
        this.type = type;
        description = type.description;
        color = type.color;
    }

    public Category(CategoryType type, String description) {
        this.type = type;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id && type == category.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }

    @Getter
    public enum CategoryType {
            INCOME("Income", "#1E90FF"),
            GROCERY("Grocery", "#32CD32"),
            HOUSEHOLD_SUPPLIES("Household supplies", "#FFD700"),
            BILLS("Bills", "#FF4500"),
            CLOTHING("Clothing", "#9370DB"),
            PETS("Pets", "#FF69B4"),
            INSURANCE("Insurance", "#00CED1"),
            SAVINGS("Savings", "#7FFF00"),
            RENT("Rent", "#DC143C"),
            UTILITIES("Utilities", "#4682B4"),
            DINING_OUT("Dining out", "#FFA07A"),
            TRANSPORTATION("Transportation", "#808080"),
            ENTERTAINMENT("Entertainment", "#8A2BE2"),
            HEALTH_CARE("Health care", "#008080"),
            EDUCATION("Education", "#00FF7F"),
            PERSONAL_CARE("Personal care", "#FF6347"),
            MISCELLANEOUS("Miscellaneous", "#708090"),
            LOAN("Loan", "#B22222"),
            PAYMENTS("Payments", "#A9A9A9"),
            FEES("Fees", "#CD853F"),
            OTHER("Other", "#696969");

        private final String description;
        private final String color;

        CategoryType(String description, String color) {
            this.description = description;
            this.color = color;
        }

    }
}
