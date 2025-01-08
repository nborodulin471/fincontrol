package ru.fincontrol.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.fincontrol.model.entity.Category;

/**
 * Репозиторий, который отвечает за работу с категориями.
 *
 * @author Бородулин Никита Петрович.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
