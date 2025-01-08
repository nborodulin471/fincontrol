package ru.fincontrol.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.fincontrol.model.entity.User;

/**
 * Сервис, который отвечает за работу с пользователями.
 *
 * @author Бородулин Никита Петрович.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}