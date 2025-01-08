package ru.fincontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import ru.fincontrol.model.OperationType;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Сущность, которая хранит операции.
 *
 * @author Бородулин Никита Петрович.
 */
@Getter
@Setter
@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Category category;

    @Enumerated(EnumType.ORDINAL)
    private OperationType operationType;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private Instant createdAt;
}
