package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.domain.SubmitBudgetPlanOperation;

import java.time.Year;

@UseCase
public class SubmitBudgetPlanUseCase {
    public UseCaseResponse<Void> submit(Year budgetPlanYear){
        return UseCaseProcessor.process( ()-> submitBudgetPlanOperation.submit(budgetPlanYear) );
    }

    public SubmitBudgetPlanUseCase(SubmitBudgetPlanOperation submitBudgetPlanOperation) {
        this.submitBudgetPlanOperation = submitBudgetPlanOperation;
    }

    private final SubmitBudgetPlanOperation submitBudgetPlanOperation;
}
