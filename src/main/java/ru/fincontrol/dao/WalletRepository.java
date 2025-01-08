package ru.fincontrol.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.fincontrol.model.entity.User;

@Repository
public interface WalletRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}