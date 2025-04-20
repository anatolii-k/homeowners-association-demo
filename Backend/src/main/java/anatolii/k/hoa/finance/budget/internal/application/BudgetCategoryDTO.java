package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.common.domain.MoneyAmount;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetCategory;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Year;

@Entity
@Table(name = "budget_category")
public class BudgetCategoryDTO {

    @Column(name = "category_year")
    private Integer year;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Column(name = "planned_amount")
    private BigDecimal plannedAmount;

    @Column(name = "allocation_type")
    private String allocationType;

    public BudgetCategoryDTO(Integer year, Long id, String name, String description, BigDecimal plannedAmount, String allocationType) {
        this.year = year;
        this.id = id;
        this.name = name;
        this.description = description;
        this.plannedAmount = plannedAmount;
        this.allocationType = allocationType;
    }

    public BudgetCategoryDTO() {
    }

    public static BudgetCategoryDTO fromDomain(BudgetCategory category){
        return new BudgetCategoryDTO( category.getYear().getValue(),
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getPlannedAmount().getAmount(),
                category.getAllocationType().toString()
                );
    }

    public BudgetCategory toDomain(){
        return new BudgetCategory(Year.of(year), id, name, description,
                MoneyAmount.of(plannedAmount),
                BudgetCategory.AllocationType.valueOf(allocationType));
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(BigDecimal plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public String getAllocationType() {
        return allocationType;
    }

    public void setAllocationType(String allocationType) {
        this.allocationType = allocationType;
    }
}
