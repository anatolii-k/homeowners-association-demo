package anatolii.k.hoa.finance.budget.internal.domain;

import java.time.Year;

public class SubmitBudgetPlanOperation {

    public void submit(Year budgetPlanYear){

        BudgetPlan budgetPlan = budgetPlanRepository.getBudgetPlanForYear(budgetPlanYear)
                .orElseThrow( ()-> BudgetException.notExists(budgetPlanYear) );

        budgetPlan.submit();
        budgetPlanRepository.save(budgetPlan);
    }

    public SubmitBudgetPlanOperation(BudgetPlanRepository budgetPlanRepository) {
        this.budgetPlanRepository = budgetPlanRepository;
    }

    private final BudgetPlanRepository budgetPlanRepository;
}
