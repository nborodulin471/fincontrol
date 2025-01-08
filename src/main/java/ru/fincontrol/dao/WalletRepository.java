package ru.fincontrol.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.fincontrol.model.entity.Wallet;

/**
 * Сервис, который отвечает за работу с кошельками.
 *
 * @author Бородулин Никита Петрович.
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}