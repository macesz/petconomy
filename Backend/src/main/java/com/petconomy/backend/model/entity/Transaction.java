package com.petconomy.backend.model.entity;

import com.petconomy.backend.controller.dto.NewTransactionDto;
import com.petconomy.backend.controller.dto.TransactionDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Transaction {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "transaction_category", // pivot table name
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )

    private Set<Category> categories;

    private BigDecimal amount;

    @ManyToOne ///  many to many
    @JoinColumn(referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_member_transaction"), nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "household_id")
    private Household household;

    // Add date for transaction
    private java.time.LocalDate date;

    public Transaction() {
    }

    public Transaction(Long id, String name, Set<Category> categories, BigDecimal amount, UserEntity userEntity) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.amount = amount;
        this.userEntity = userEntity;
    }

    public Transaction(TransactionDto dto) {
        id = dto.id();
        name = dto.name();
    }

    public Transaction(NewTransactionDto dto) {
        name = dto.name();
        amount = dto.amount();
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + categories +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id) && amount == that.amount &&
                Objects.equals(name, that.name) &&
                Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, categories, amount);
    }
}
