package ru.fincontrol.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.fincontrol.dao.UserRepository;
import ru.fincontrol.model.entity.User;

/**
 * Сервис, который отвечает за получения данных текущего пользователя.
 *
 * @author Бородулин Никита Петрович.
 */
@Service
@RequiredArgsConstructor
public class AuthFetcher {

    private final UserRepository userRepository;

    /**
     * Получает текущего пользователя.
     */
    public User getCurrentAuthUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        return userRepository.findByUsername(authentication.getName());
    }

}
