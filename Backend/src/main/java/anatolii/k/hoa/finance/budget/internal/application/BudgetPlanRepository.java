package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlan;

import java.time.Year;
import java.util.Optional;

public interface BudgetPlanRepository {
    Optional<BudgetPlan> getBudgetPlanForYear(Year year);

    void save(BudgetPlan budgetPlan);
}
