package anatolii.k.hoa.finance.budget.internal.domain;

import anatolii.k.hoa.common.domain.MoneyAmount;

import java.time.Year;
import java.util.LinkedList;
import java.util.List;

public class BudgetPlan {

    public enum Status{
        DRAFT,
        SUBMITTED,
        APPROVED
    }

    private Long id;
    private Year year;
    private List<String> notes;
    private List<BudgetCategory> categories = new LinkedList<>();
    private Status budgetPlanStatus;

    public MoneyAmount getTotal(){
        return categories.stream()
                .map(BudgetCategory::getPlannedAmount)
                .reduce( MoneyAmount.zero(), MoneyAmount::add );
    }
}
