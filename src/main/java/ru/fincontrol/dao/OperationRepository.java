package ru.fincontrol.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.fincontrol.model.entity.Operation;

/**
 * Репозиторий, который отвечает за работу с операциями.
 *
 * @author Бородулин Никита Петрович.
 */
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
}
