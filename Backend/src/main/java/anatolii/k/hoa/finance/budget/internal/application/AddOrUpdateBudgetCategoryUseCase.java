package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.domain.AddOrUpdateBudgetCategoryOperation;

import java.time.Year;

@UseCase
public class AddOrUpdateBudgetCategoryUseCase {

    public UseCaseResponse<Void> addOrUpdate(Integer budgetYear, BudgetCategoryDTO budgetCategoryDTO){
        return UseCaseProcessor.process(
                ()-> addOrUpdateBudgetCategoryOperation.addOrUpdate(Year.of(budgetYear), budgetCategoryDTO.toDomain())
        );
    }

    public AddOrUpdateBudgetCategoryUseCase(AddOrUpdateBudgetCategoryOperation addOrUpdateBudgetCategoryOperation) {
        this.addOrUpdateBudgetCategoryOperation = addOrUpdateBudgetCategoryOperation;
    }

    private final AddOrUpdateBudgetCategoryOperation addOrUpdateBudgetCategoryOperation;
}
