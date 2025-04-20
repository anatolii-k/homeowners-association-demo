package anatolii.k.hoa.finance.budget.internal.infrastructure.db;

import anatolii.k.hoa.finance.budget.internal.application.BudgetPlanDTO;
import org.springframework.data.repository.CrudRepository;

interface BudgetPlanRepositoryJPA extends CrudRepository<BudgetPlanDTO,Integer> {
}
