package anatolii.k.hoa.finance.budget.internal.domain;

import java.time.Year;

public class AddOrUpdateBudgetCategoryOperation {

    public void addOrUpdate(Year budgetYear, BudgetCategory category){

        BudgetPlan budgetPlan = budgetPlanRepository.getBudgetPlanForYear(budgetYear)
                .orElseThrow( ()-> BudgetException.notExists(budgetYear) );

        budgetPlan.addOrUpdateCategory(category);
        budgetPlanRepository.save(budgetPlan);
    }

    public AddOrUpdateBudgetCategoryOperation(BudgetPlanRepository budgetPlanRepository) {
        this.budgetPlanRepository = budgetPlanRepository;
    }

    private final BudgetPlanRepository budgetPlanRepository;
}
