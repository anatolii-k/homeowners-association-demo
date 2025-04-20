package unit_test.finance.budget.domain;

import anatolii.k.hoa.common.domain.CommonException;
import anatolii.k.hoa.common.domain.MoneyAmount;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetCategory;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetException;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlan;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.LinkedList;
import java.util.List;

public class BudgetPlanTest {

    @Test
    void whenGetTotal_thenCalculateSum(){

        Year year = Year.now();
        BudgetPlan budget = BudgetPlan.create(year,
                List.of( new BudgetCategory(year, null, "Maintenance", "", MoneyAmount.of(110.22), BudgetCategory.AllocationType.PER_UNIT),
                         new BudgetCategory(year, null, "Heating", "", MoneyAmount.of(220.33), BudgetCategory.AllocationType.PER_UNIT_AREA)),
                BudgetPlan.Status.DRAFT);

        assertThat(budget.getTotal()).isEqualTo(MoneyAmount.of(330.55));
    }

    @Test
    void whenCreateBudgetPlanWithEmptyYear_thenException(){

        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> BudgetPlan.create(null, new LinkedList<>(), BudgetPlan.Status.DRAFT));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(BudgetException.ErrorCode.YEAR_IS_REQUIRED);
    }


}
