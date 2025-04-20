package unit_test.finance.budget.domain;

import anatolii.k.hoa.common.domain.CommonException;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetException;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlan;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.Year;

public class BudgetPlanStatusTest {

    @Test
    void givenBudgetPlanDraft_whenSubmit_thenOk(){
        BudgetPlan budget = BudgetPlan.create(Year.now(), null, null);

        assertThat(budget.isDraft()).isTrue();
        assertThat(budget.isSubmitted()).isFalse();
        assertThat(budget.isApproved()).isFalse();
        assertThat(budget.isFinalized()).isFalse();
        assertThat(budget.getStatus()).isEqualTo(BudgetPlan.Status.DRAFT);

        budget.submit();

        assertThat(budget.isDraft()).isFalse();
        assertThat(budget.isSubmitted()).isTrue();
        assertThat(budget.isApproved()).isFalse();
        assertThat(budget.isFinalized()).isTrue();
        assertThat(budget.getStatus()).isEqualTo(BudgetPlan.Status.SUBMITTED);
    }

    @Test
    void givenBudgetPlanDraft_whenReject_thenOk(){
        BudgetPlan budget = BudgetPlan.create(Year.now(), null, null);
        budget.reject();

        assertThat(budget.isDraft()).isTrue();
    }

    @Test
    void givenBudgetPlanDraft_whenApprove_thenException(){
        BudgetPlan budget = BudgetPlan.create(Year.now(), null, null);

        CommonException exception = catchThrowableOfType(CommonException.class,
                budget::approve);

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(BudgetException.ErrorCode.INCORRECT_STATUS.toString());
    }


    @Test
    void givenBudgetPlanSubmitted_whenApprove_thenOk(){
        BudgetPlan budget = BudgetPlan.create(Year.now(), null, null);
        budget.submit();
        budget.approve();

        assertThat(budget.isDraft()).isFalse();
        assertThat(budget.isSubmitted()).isFalse();
        assertThat(budget.isApproved()).isTrue();
        assertThat(budget.isFinalized()).isTrue();
        assertThat(budget.getStatus()).isEqualTo(BudgetPlan.Status.APPROVED);
    }

    @Test
    void givenBudgetPlanSubmitted_whenReject_thenOk(){
        BudgetPlan budget = BudgetPlan.create(Year.now(), null, null);
        budget.submit();
        budget.reject();

        assertThat(budget.isDraft()).isTrue();
    }

    @Test
    void givenBudgetPlanApproved_whenReject_thenException(){
        BudgetPlan budget = BudgetPlan.create(Year.now(), null, null);
        budget.submit();
        budget.approve();

        CommonException exception = catchThrowableOfType(CommonException.class,
                budget::reject);

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(BudgetException.ErrorCode.BUDGET_ALREADY_FINALIZED.toString());
    }

}
