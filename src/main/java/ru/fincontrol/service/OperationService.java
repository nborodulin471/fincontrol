package ru.fincontrol.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.fincontrol.dao.OperationRepository;
import ru.fincontrol.model.entity.Operation;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class OperationCService {

    private final AuthFetcher authFetcher;
    private final OperationRepository operationRepository;
    private final CategoryService categoryService;

    public void addOperation(long category, long amount) {
//        checkLimit(category, amount);

        Operation operation = new Operation();
        operation.setUser(authFetcher.getCurrentAuthUser());
        operation.setCategory(categoryService.fetchCategory(category));
        operation
        operation.setAmount(amount);

        operationRepository.save(operation);
    }

    public Collection<Operation> getOperationByUser() {
        return operationRepository.findAllByUser(authFetcher.getCurrentAuthUser());
    }

    public long getCurrentTotalByCategory(String user, String category) {
//        List<Operation> expenses = operationRepository.findByUser(user);
//        return expenses.stream()
//                .filter(expense -> expense.getCategory().equals(category))
//                .map(Expense::getAmount)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return 100L; // todo
    }

    private Long getLimitForCategory(String category) {
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
        return 100L; // todo
    }

    private void checkLimit(String category, long amount) {
        var limit = getLimitForCategory(category);
        var currentTotal = getCurrentTotalByCategory(authFetcher.getCurrentAuthUser().getUsername(), category);
        if ((currentTotal + amount) > limit) {
            // todo обработать эту ситуацию когда идет преывшение лимита
        }
    }

}
