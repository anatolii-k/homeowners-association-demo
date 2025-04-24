package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.domain.RejectBudgetPlanOperation;

import java.time.Year;

@UseCase
public class RejectBudgetPlanUseCase {

    public UseCaseResponse<Void> reject(Year budgetPlanYear){
        return UseCaseProcessor.process( ()-> rejectBudgetPlanOperation.reject(budgetPlanYear));
    }

    public RejectBudgetPlanUseCase(RejectBudgetPlanOperation rejectBudgetPlanOperation) {
        this.rejectBudgetPlanOperation = rejectBudgetPlanOperation;
    }

    private final RejectBudgetPlanOperation rejectBudgetPlanOperation;
}
