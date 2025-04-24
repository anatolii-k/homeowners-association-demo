package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.domain.DeleteBudgetCategoryOperation;

import java.time.Year;

@UseCase
public class DeleteBudgetCategoryUseCase {

    public UseCaseResponse<Void> delete(Integer year, Long categoryId){
        return UseCaseProcessor.process(
                ()-> deleteBudgetCategoryOperation.delete(Year.of(year), categoryId)
        );
    }

    public DeleteBudgetCategoryUseCase(DeleteBudgetCategoryOperation deleteBudgetCategoryOperation) {
        this.deleteBudgetCategoryOperation = deleteBudgetCategoryOperation;
    }

    private final DeleteBudgetCategoryOperation deleteBudgetCategoryOperation;
}
