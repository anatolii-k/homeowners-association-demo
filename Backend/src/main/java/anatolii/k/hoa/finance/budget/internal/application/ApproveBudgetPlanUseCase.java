package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.domain.ApproveBudgetPlanOperation;

import java.time.Year;

@UseCase
public class ApproveBudgetPlanUseCase {

    public UseCaseResponse<Void> approve(Year budgetPlanYear){
        return UseCaseProcessor.process( ()-> approveBudgetPlanOperation.approve(budgetPlanYear));
    }

    public ApproveBudgetPlanUseCase(ApproveBudgetPlanOperation approveBudgetPlanOperation) {
        this.approveBudgetPlanOperation = approveBudgetPlanOperation;
    }

    private final ApproveBudgetPlanOperation approveBudgetPlanOperation;
}
