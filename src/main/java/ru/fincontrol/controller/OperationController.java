package ru.fincontrol.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.fincontrol.model.dto.CategoryDto;
import ru.fincontrol.model.dto.OperationDto;
import ru.fincontrol.model.OperationType;
import ru.fincontrol.service.CategoryService;
import ru.fincontrol.service.OperationService;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Сервис, который отвечает за работу с операциями.
 *
 * @author Бородулин Никита Петрович.
 */
@Controller
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;
    private final CategoryService categoryService;

    /**
     * Отображает главную страницу.
     */
    @GetMapping("/")
    public String view(Model model) {
        model.addAttribute("operations", operationService.getOperation().stream()
                .map(OperationDto::map)
                .toList());
        model.addAttribute("categories", categoryService.findAllForWallet().stream()
                .map(CategoryDto::map)
                .toList());
        model.addAttribute("typeOperations", Arrays.stream(OperationType.values())
                .map(OperationType::getName)
                .toList());

        var expense = operationService.getCurrentSum(OperationType.EXPENSE);
        var profit = operationService.getCurrentSum(OperationType.PROFIT);
        model.addAttribute("totalExpense", expense);
        model.addAttribute("totalProfit", profit);
        model.addAttribute("totalBalance", profit.subtract(expense));

        return "index";
    }

    /**
     * Добавляет операцию
     */
    @PostMapping("/addOperation")
    public String addOperation(@RequestParam long category, @RequestParam BigDecimal amount, @RequestParam String type) {
        operationService.addOperation(category, amount, type);
        return "redirect:/";
    }

    /**
     * Удаляет операцию.
     */
    @PostMapping("/deleteOperation/{id}")
    public String deleteOperation(@PathVariable long id) {
        operationService.deleteOperation(id);
        return "redirect:/";
    }

}


