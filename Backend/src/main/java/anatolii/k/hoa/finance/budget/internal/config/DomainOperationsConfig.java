package anatolii.k.hoa.finance.budget.internal.config;

import anatolii.k.hoa.finance.budget.internal.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainOperationsConfig {

    @Bean
    SubmitBudgetPlanOperation submitBudgetPlanOperation(BudgetPlanRepository budgetPlanRepository){
        return new SubmitBudgetPlanOperation(budgetPlanRepository);
    }

    @Bean
    ApproveBudgetPlanOperation approveBudgetPlanOperation(BudgetPlanRepository budgetPlanRepository){
        return new ApproveBudgetPlanOperation(budgetPlanRepository);
    }
    @Bean
    RejectBudgetPlanOperation rejectBudgetPlanOperation(BudgetPlanRepository budgetPlanRepository){
        return new RejectBudgetPlanOperation(budgetPlanRepository);
    }

    @Bean
    AddOrUpdateBudgetCategoryOperation addOrUpdateBudgetCategoryOperation(BudgetPlanRepository budgetPlanRepository){
        return new AddOrUpdateBudgetCategoryOperation(budgetPlanRepository);
    }

    @Bean
    CreateBudgetPlanOperation createBudgetPlanOperation(BudgetPlanRepository budgetPlanRepository){
        return new CreateBudgetPlanOperation(budgetPlanRepository);
    }

    @Bean
    DeleteBudgetCategoryOperation deleteBudgetCategoryOperation(BudgetPlanRepository budgetPlanRepository){
        return new DeleteBudgetCategoryOperation(budgetPlanRepository);
    }
}
