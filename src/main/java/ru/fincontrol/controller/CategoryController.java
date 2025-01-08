package ru.fincontrol.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.fincontrol.service.CategoryService;

import java.math.BigDecimal;

/**
 * Контроллер, который отвечает за работу с категориями.
 *
 * @author Бородулин Никита Петрович.
 */
@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Показывает доступные категории.
     */
    @GetMapping("/showCategory")
    public String showCategory(Model model) {
        model.addAttribute("categories", categoryService.findAllForWallet());

        return "category";
    }

    /**
     * Создает категорию.
     */
    @PostMapping("/createCategory")
    public String addCategory(@RequestParam String name, @RequestParam BigDecimal limit) {
        categoryService.addCategory(name, limit);

        return "redirect:/showCategory";
    }

    /**
     * Удаляет категорию по ее идентификатору.
     */
    @PostMapping("/deleteCategory/{id}")
    public String delete(@PathVariable long id) {
        categoryService.deleteCategory(id);

        return "redirect:/showCategory";
    }

}
