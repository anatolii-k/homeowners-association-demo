package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.finance.budget.internal.domain.BudgetCategory;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlan;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

@Entity
@Table(name = "budget_plan")
public class BudgetPlanDTO {

    @Id
    @Column(name = "budget_year")
    private Integer year;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "category_year")
    private List<BudgetCategoryDTO> categories;
    private String status;

    @Transient
    private BigDecimal total;

    public BudgetPlanDTO(Integer year, List<BudgetCategoryDTO> categories, String status) {
        this.year = year;
        this.categories = categories;
        this.status = status;
    }

    public BudgetPlanDTO() {
    }

    public static BudgetPlanDTO fromDomain(BudgetPlan budgetPlan){
        List<BudgetCategoryDTO> categories = budgetPlan.getCategories()
                .stream()
                .map(BudgetCategoryDTO::fromDomain)
                .toList();
        var budgetDTO = new BudgetPlanDTO(budgetPlan.getYear().getValue(),
                categories, budgetPlan.getStatus().toString());
        budgetDTO.setTotal( budgetPlan.getTotal().getAmount() );
        return budgetDTO;
    }

    public BudgetPlan toDomain(){
        List<BudgetCategory> domainCategories = categories.stream()
                .map(BudgetCategoryDTO::toDomain)
                .toList();

        return BudgetPlan.create(Year.of(year), domainCategories, BudgetPlan.Status.valueOf(status));
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<BudgetCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<BudgetCategoryDTO> categories) {
        this.categories = categories;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
