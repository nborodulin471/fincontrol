package ru.fincontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Сущность, которая хранит кошельки.
 *
 * @author Бородулин Никита Петрович.
 */
@Getter
@Setter
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
    List<Operation> operations;
    @OneToOne
    private User user;
    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
    List<Category> categories;
}
