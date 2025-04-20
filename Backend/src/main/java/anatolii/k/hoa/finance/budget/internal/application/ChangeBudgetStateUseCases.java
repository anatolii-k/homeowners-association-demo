package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetException;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlan;

import java.time.Year;
import java.util.Optional;

@UseCase
public class ChangeBudgetStateUseCases {

    public enum Action{
        submit,
        approve,
        reject
    }

    public UseCaseResponse<Void> changeState( Integer year, String actionName ){
        return UseCaseProcessor.process( ()-> changeStateImpl(year, actionName) );
    }

    private void changeStateImpl(Integer year, String actionString){
        Action action = Action.valueOf(actionString);

        Optional<BudgetPlan> budgetPlan = budgetPlanRepository.getBudgetPlanForYear(Year.of(year));
        if( budgetPlan.isEmpty() ){
            throw BudgetException.notExists(year);
        }

        switch (action){
            case submit -> budgetPlan.get().submit();
            case approve -> budgetPlan.get().approve();
            case reject -> budgetPlan.get().reject();
        }

        budgetPlanRepository.save(budgetPlan.get());
    }

    public ChangeBudgetStateUseCases(BudgetPlanRepository budgetPlanRepository) {
        this.budgetPlanRepository = budgetPlanRepository;
    }

    private final BudgetPlanRepository budgetPlanRepository;
}
