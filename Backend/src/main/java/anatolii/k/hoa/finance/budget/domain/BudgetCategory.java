package anatolii.k.hoa.finance.budget.domain;

import anatolii.k.hoa.common.domain.MoneyAmount;

public class BudgetCategory {

    enum AllocationType{
        PER_UNIT,
        PER_UNIT_AREA
    }

    private Long id;
    private String name;
    private String description;
    private MoneyAmount plannedAmount;
    private AllocationType allocationType;

    public MoneyAmount getPlannedAmount() {
        return plannedAmount;
    }
}
