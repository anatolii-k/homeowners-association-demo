package anatolii.k.hoa.finance.budget.internal.domain;

public class CreateBudgetPlanOperation {

    public void create(BudgetPlan budgetPlan){
        if( budgetPlanRepository.doesBudgetPlanExist(budgetPlan.getYear()) ){
            throw BudgetException.alreadyExists(budgetPlan.getYear());
        }
        if(!budgetPlan.isDraft()){
            throw BudgetException.newBudgetNotDraft(budgetPlan.getStatus());
        }
        budgetPlanRepository.save(budgetPlan);
    }

    public CreateBudgetPlanOperation(BudgetPlanRepository budgetPlanRepository) {
        this.budgetPlanRepository = budgetPlanRepository;
    }

    private final BudgetPlanRepository budgetPlanRepository;
}
