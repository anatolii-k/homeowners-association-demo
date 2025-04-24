package anatolii.k.hoa.finance.budget.internal.domain;

import java.time.Year;

public class DeleteBudgetCategoryOperation {

    public void delete(Year budgetYear, Long categoryId){

        BudgetPlan budgetPlan = budgetPlanRepository.getBudgetPlanForYear(budgetYear)
                .orElseThrow( ()-> BudgetException.notExists(budgetYear) );

        budgetPlan.deleteCategory(categoryId);
        budgetPlanRepository.save(budgetPlan);
    }

    public DeleteBudgetCategoryOperation(BudgetPlanRepository budgetPlanRepository) {
        this.budgetPlanRepository = budgetPlanRepository;
    }

    private final BudgetPlanRepository budgetPlanRepository;
}
