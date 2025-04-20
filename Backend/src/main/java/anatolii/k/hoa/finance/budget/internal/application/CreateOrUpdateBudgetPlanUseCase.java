package anatolii.k.hoa.finance.budget.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetException;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlan;

import java.util.Optional;

@UseCase
public class CreateOrUpdateBudgetPlanUseCase {

    public enum BudgetUpdateResult{
        CREATED,
        UPDATED
    }

    public UseCaseResponse<BudgetUpdateResult> createOrUpdate(BudgetPlanDTO newBudget){
        return UseCaseProcessor.process(
                () -> createOrUpdateImpl(newBudget.toDomain())
        );
    }

    private BudgetUpdateResult createOrUpdateImpl(BudgetPlan newBudget){
        Optional<BudgetPlan> existingBudget = budgetPlanRepository.getBudgetPlanForYear(newBudget.getYear());

        boolean doesBudgetExists = existingBudget.isPresent();

        if(doesBudgetExists){
            checkUpdateIsAllowed(existingBudget.get(), newBudget);
        }
        budgetPlanRepository.save(newBudget);

        return doesBudgetExists ? BudgetUpdateResult.UPDATED : BudgetUpdateResult.CREATED;
    }

    private void checkUpdateIsAllowed( BudgetPlan currentBudget, BudgetPlan newBudget ){
        if(currentBudget.isFinalized()){
            throw BudgetException.budgetAlreadyFinalized();
        }
        if(!currentBudget.getStatus().equals(newBudget.getStatus())){
            throw BudgetException.changeStatusNotAllowed();
        }
    }

    public CreateOrUpdateBudgetPlanUseCase(BudgetPlanRepository budgetPlanRepository) {
        this.budgetPlanRepository = budgetPlanRepository;
    }

    private final BudgetPlanRepository budgetPlanRepository;
}
