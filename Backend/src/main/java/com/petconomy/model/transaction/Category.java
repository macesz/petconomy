package com.petconomy.model.transaction;
import com.petconomy.model.user.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column()
    private CategoryType type;

    private boolean defaultValue;

    private String name;

    private String color;

    private BigDecimal targetAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Category() {
    }

    public Category(CategoryType type) {
        this.type = type;
        this.name = type.name;
        this.color = type.color;
        this.defaultValue = true;
    }

    public Category(String name, String color, Member member) {
        this.name = name;
        this.color = (color == null || color.isEmpty() ? CategoryType.OTHER.color : color );
        this.defaultValue = false;
        this.type = null;
        this.member = member;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", type=" + type +
                ", description='" + name + '\'' +
                '}';
    }

    // Keep the category types as an enum
    public enum CategoryType {
            INCOME("Income", "#1E90FF"),
            GROCERY("Grocery", "#32CD32"),
            HOUSEHOLD_SUPPLIES("Household supplies", "#FFD700"),
            BILLS("Bills", "#FF4500"),
            CLOTHING("Clothing", "#9370DB"),
            PETS("Pets", "#FF69B4"),
            INSURANCE("Insurance", "#00CED1"),
            SAVINGS("Savings", "#7FFF00"),
            INVESTMENT("Investment", "#FF8C00"),
            LOAN("Loan", "#BA55D3"),
            RENT("Rent", "#DC143C"),
            UTILITIES("Utilities", "#4682B4"),
            DINING_OUT("Dining out", "#FFA07A"),
            TRANSPORTATION("Transportation", "#808080"),
            ENTERTAINMENT("Entertainment", "#8A2BE2"),
            HEALTH_CARE("Health care", "#008080"),
            EDUCATION("Education", "#00FF7F"),
            PERSONAL_CARE("Personal care", "#FF6347"),
            MISCELLANEOUS("Miscellaneous", "#708090"),
            PAYMENTS("Payments", "#A9A9A9"),
            FEES("Fees", "#CD853F"),
            OTHER("Other", "#696969");

        private final String name;
        private final String color;

        CategoryType(String name, String color) {
            this.name = name;
            this.color = color;
        }
    }
}
