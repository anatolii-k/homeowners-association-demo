package anatolii.k.hoa.finance.budget.internal.domain;

import java.time.Year;

public class ApproveBudgetPlanOperation {

    public void approve(Year budgetPlanYear){

        BudgetPlan budgetPlan = budgetPlanRepository.getBudgetPlanForYear(budgetPlanYear)
                .orElseThrow( ()-> BudgetException.notExists(budgetPlanYear) );

        budgetPlan.approve();
        budgetPlanRepository.save(budgetPlan);
    }

    public ApproveBudgetPlanOperation(BudgetPlanRepository budgetPlanRepository) {
        this.budgetPlanRepository = budgetPlanRepository;
    }

    private final BudgetPlanRepository budgetPlanRepository;
}
