package unit_test.finance.budget.domain;

import anatolii.k.hoa.common.domain.CommonException;
import anatolii.k.hoa.common.domain.MoneyAmount;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetCategory;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetException;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlan;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        assertThat(exception.getErrorCode()).isEqualTo(BudgetException.ErrorCode.YEAR_IS_REQUIRED.toString());
    }

    @Test
    void givenDraftBudgetPlan_whenAddCategory_thenOk(){

        Year year = Year.now();
        ArrayList<BudgetCategory> categories = new ArrayList<>();
        categories.add(new BudgetCategory(year, null, "one", "", MoneyAmount.of(110.22), BudgetCategory.AllocationType.PER_UNIT));
        categories.add(new BudgetCategory(year, null, "two", "", MoneyAmount.of(220.22), BudgetCategory.AllocationType.PER_UNIT));
        BudgetPlan budget = BudgetPlan.create( year, categories, BudgetPlan.Status.DRAFT );

        int categoriesCount = budget.getCategories().size();
        String newCategoryName = "New Category";

        budget.addOrUpdateCategory(
                new BudgetCategory(year, null, newCategoryName, "", MoneyAmount.of(1000), BudgetCategory.AllocationType.PER_UNIT)
        );

        assertThat(budget.getCategories().size()).isEqualTo(categoriesCount + 1);

        Optional<BudgetCategory> newCategory = budget.getCategories()
                .stream()
                .filter( category -> category.getName().equals(newCategoryName))
                .findAny();

        assertThat(newCategory.isPresent()).isTrue();
    }

    @Test
    void givenDraftBudgetPlan_whenUpdateCategory_thenOk(){

        Year year = Year.now();
        ArrayList<BudgetCategory> categories = new ArrayList<>();
        categories.add(new BudgetCategory(year, 1L, "one", "", MoneyAmount.of(110.22), BudgetCategory.AllocationType.PER_UNIT));
        categories.add(new BudgetCategory(year, 2L, "two", "", MoneyAmount.of(220.22), BudgetCategory.AllocationType.PER_UNIT));
        BudgetPlan budget = BudgetPlan.create( year, categories, BudgetPlan.Status.DRAFT );


        int categoriesCount = budget.getCategories().size();
        BudgetCategory categoryForUpdate = new BudgetCategory(year, 2L, "Updated Category", "Some description", MoneyAmount.of(1000.00), BudgetCategory.AllocationType.PER_UNIT);

        budget.addOrUpdateCategory( categoryForUpdate);

        assertThat(budget.getCategories().size()).isEqualTo(categoriesCount);

        Optional<BudgetCategory> newCategory = budget.getCategories()
                .stream()
                .filter( category -> category.getId().equals(2L))
                .findAny();

        assertThat(newCategory.isPresent()).isTrue();
        assertThat(newCategory.get().getName()).isEqualTo(categoryForUpdate.getName());
        assertThat(newCategory.get().getPlannedAmount()).isEqualTo(categoryForUpdate.getPlannedAmount());
    }

    @Test
    void givenFinalizedBudgetPlan_whenAddCategory_thenException(){

        Year year = Year.now();
        ArrayList<BudgetCategory> categories = new ArrayList<>();
        categories.add(new BudgetCategory(year, null, "one", "", MoneyAmount.of(110.22), BudgetCategory.AllocationType.PER_UNIT));
        categories.add(new BudgetCategory(year, null, "two", "", MoneyAmount.of(220.22), BudgetCategory.AllocationType.PER_UNIT));
        BudgetPlan budget = BudgetPlan.create( year, categories, BudgetPlan.Status.DRAFT );
        budget.submit();

        int categoriesCount = budget.getCategories().size();

        BudgetCategory newCategory = new BudgetCategory(year, null, "Updated Category", "Some description", MoneyAmount.of(1000.00), BudgetCategory.AllocationType.PER_UNIT);

        CommonException exception = catchThrowableOfType( CommonException.class,
                () -> budget.addOrUpdateCategory(newCategory));


        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(BudgetException.ErrorCode.BUDGET_ALREADY_FINALIZED.toString());
    }

    @Test
    void givenDraftBudgetPlan_whenDeleteCategory_thenOk(){

        Year year = Year.now();
        ArrayList<BudgetCategory> categories = new ArrayList<>();
        categories.add(new BudgetCategory(year, 1L, "one", "", MoneyAmount.of(110.22), BudgetCategory.AllocationType.PER_UNIT));
        categories.add(new BudgetCategory(year, 2L, "two", "", MoneyAmount.of(220.22), BudgetCategory.AllocationType.PER_UNIT));
        BudgetPlan budget = BudgetPlan.create( year, categories, BudgetPlan.Status.DRAFT );


        int categoriesCount = budget.getCategories().size();

        budget.deleteCategory(1L);

        assertThat(budget.getCategories().size()).isEqualTo(1);

        BudgetCategory existingCategory = budget.getCategories().getFirst();
        assertThat(existingCategory.getId()).isEqualTo(2L);
    }

    @Test
    void givenFinalizedBudgetPlan_whenDeleteCategory_thenException(){

        Year year = Year.now();
        ArrayList<BudgetCategory> categories = new ArrayList<>();
        categories.add(new BudgetCategory(year, 1L, "one", "", MoneyAmount.of(110.22), BudgetCategory.AllocationType.PER_UNIT));
        categories.add(new BudgetCategory(year, 2L, "two", "", MoneyAmount.of(220.22), BudgetCategory.AllocationType.PER_UNIT));
        BudgetPlan budget = BudgetPlan.create( year, categories, BudgetPlan.Status.DRAFT );
        budget.submit();

        CommonException exception = catchThrowableOfType( CommonException.class,
                () -> budget.deleteCategory(1L));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(BudgetException.ErrorCode.BUDGET_ALREADY_FINALIZED.toString());
    }
}
