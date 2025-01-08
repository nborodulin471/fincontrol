package ru.fincontrol.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import ru.fincontrol.service.BalanceService;

/**
 * Контроллер, отвечающий за работу с остатками по категориям.
 *
 * @author Бородулин Никита Петрович.
 */
@Controller
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    /**
     * Показывает остатки по категориям.
     */
    @GetMapping("/showBalance")
    public String showBalance(Model model) {
        model.addAttribute("balance", balanceService.showBalance());

        return "balance";
    }

}
