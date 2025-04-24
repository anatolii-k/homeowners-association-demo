package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.domain.CreateBudgetPlanOperation;


@UseCase
public class CreateBudgetPlanUseCase {

    public UseCaseResponse<Void> create(BudgetPlanDTO newBudget){
        return UseCaseProcessor.process(
                () -> createBudgetPlanOperation.create(newBudget.toDomain())
        );
    }

    public CreateBudgetPlanUseCase(CreateBudgetPlanOperation createBudgetPlanOperation) {
        this.createBudgetPlanOperation = createBudgetPlanOperation;
    }

    private final CreateBudgetPlanOperation createBudgetPlanOperation;
}
