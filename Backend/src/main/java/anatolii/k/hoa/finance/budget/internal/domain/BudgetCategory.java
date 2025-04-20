package anatolii.k.hoa.finance.budget.internal.domain;

import anatolii.k.hoa.common.domain.MoneyAmount;

import java.time.Year;

public class BudgetCategory {

    public enum AllocationType{
        PER_UNIT,
        PER_UNIT_AREA
    }

    private final Year year;
    private final Long id;
    private String name;
    private String description;
    private MoneyAmount plannedAmount;
    private AllocationType allocationType;

    public BudgetCategory(Year year, Long id, String name, String description, MoneyAmount plannedAmount, AllocationType allocationType) {
        this.year = year;
        this.id = id;
        this.name = name;
        this.description = description;
        this.plannedAmount = plannedAmount;
        this.allocationType = allocationType;
    }

    public Year getYear() {
        return year;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MoneyAmount getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(MoneyAmount plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public AllocationType getAllocationType() {
        return allocationType;
    }

    public void setAllocationType(AllocationType allocationType) {
        this.allocationType = allocationType;
    }
}
