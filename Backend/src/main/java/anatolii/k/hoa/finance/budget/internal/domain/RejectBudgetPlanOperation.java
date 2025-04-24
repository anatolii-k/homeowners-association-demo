package anatolii.k.hoa.finance.budget.internal.domain;

import java.time.Year;

public class RejectBudgetPlanOperation {

    public void reject(Year budgetPlanYear){

        BudgetPlan budgetPlan = budgetPlanRepository.getBudgetPlanForYear(budgetPlanYear)
                .orElseThrow( ()-> BudgetException.notExists(budgetPlanYear) );

        budgetPlan.reject();
        budgetPlanRepository.save(budgetPlan);
    }

    public RejectBudgetPlanOperation(BudgetPlanRepository budgetPlanRepository) {
        this.budgetPlanRepository = budgetPlanRepository;
    }

    private final BudgetPlanRepository budgetPlanRepository;
}
