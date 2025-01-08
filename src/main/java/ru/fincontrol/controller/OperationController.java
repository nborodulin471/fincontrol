//package ru.fincontrol.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import lombok.RequiredArgsConstructor;
//import ru.fincontrol.model.Expense;
//import ru.fincontrol.service.ExpenseService;
//
//import java.math.BigDecimal;
//import java.security.Principal;
//
//@Controller
//@RequiredArgsConstructor
//public class ExpenseController {
//
//    private final ExpenseService expenseService;
//
//    @GetMapping("/")
//    public String index() {
//        return "index";
//    }
//
//    @GetMapping("/add-expense")
//    public String addExpenseForm() {
//        return "add-expense";
//    }
//
//    @PostMapping("/add-expense")
//    public String addExpense(@RequestParam String category, @RequestParam BigDecimal amount, Principal principal) {
//        // Получите лимит для категории (например, из базы данных или конфигурации)
//        BigDecimal limit = getLimitForCategory(category); // Реализуйте этот метод
//
//        // Получите текущие расходы по категории
//        BigDecimal currentTotal = expenseService.getCurrentTotalByCategory(principal.getName(), category);
//
//        if (currentTotal.add(amount).compareTo(limit) > 0) {
//            // Обработка превышения лимита
//            return "redirect:/add-expense?error=limitExceeded";
//        }
//
//
//        Expense expense = new Expense();
//        expense.setUser(principal.getName());
//        expense.setCategory(category);
//        expense.setAmount(amount);
//        expenseService.saveExpense(expense);
//
//        return "redirect:/view-expenses";
//
//    }
//
//    @GetMapping("/view-expenses")
//    public String viewExpenses(Model model, Principal principal) {
//        String user = principal.getName();
//        model.addAttribute("expenses", expenseService.getExpensesByUser(user));
//        return "view-expenses";
//    }
//
//    private BigDecimal getLimitForCategory(String category) {
//        switch (category.toLowerCase()) {
//            case "food":
//                return new BigDecimal("300.00");
//            case "transport":
//                return new BigDecimal("150.00");
//            case "entertainment":
//                return new BigDecimal("200.00");
//            default:
//                return BigDecimal.ZERO; // Без лимита
//        }
//    }
//
//}
//
//
