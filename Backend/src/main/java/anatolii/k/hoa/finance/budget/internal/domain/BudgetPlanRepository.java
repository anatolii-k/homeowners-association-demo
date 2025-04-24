package anatolii.k.hoa.finance.budget.internal.domain;

import java.time.Year;
import java.util.Optional;

public interface BudgetPlanRepository {
    Optional<BudgetPlan> getBudgetPlanForYear(Year year);

    void save(BudgetPlan budgetPlan);

    boolean doesBudgetPlanExist(Year year);
}
