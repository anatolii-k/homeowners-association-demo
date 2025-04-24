package anatolii.k.hoa.finance.budget.internal.infrastructure.db;

import anatolii.k.hoa.finance.budget.internal.application.BudgetPlanDTO;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlanRepository;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlan;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.Optional;

@Repository
public class BudgetPlanRepositoryImpl implements BudgetPlanRepository {

    @Override
    public Optional<BudgetPlan> getBudgetPlanForYear(Year year) {
        Optional<BudgetPlanDTO> budgetDTO = jpaRepository.findById(year.getValue());
        return budgetDTO.map(BudgetPlanDTO::toDomain);
    }

    @Override
    public void save(BudgetPlan budgetPlan) {
        jpaRepository.save(BudgetPlanDTO.fromDomain(budgetPlan));
    }

    @Override
    public boolean doesBudgetPlanExist(Year year) {
        return jpaRepository.existsById(year.getValue());
    }

    public BudgetPlanRepositoryImpl(BudgetPlanRepositoryJPA jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private final BudgetPlanRepositoryJPA jpaRepository;
}
