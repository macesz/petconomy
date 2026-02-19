package com.petconomy.backend.model.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Closer {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "house_id")
    private Household household;

    private LocalDate date;

    private BigDecimal amount;

    public Closer() {}

    public Closer(Household household, LocalDate date, BigDecimal amount) {
        this.household = household;
        this.date = date;
        this.amount = amount;
    }


    public Closer(BigDecimal balance, LocalDate closerDate) {
        this.amount = balance;
        this.date = closerDate;
    }
}
