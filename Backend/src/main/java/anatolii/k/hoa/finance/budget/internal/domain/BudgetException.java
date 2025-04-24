package anatolii.k.hoa.finance.budget.internal.domain;

import anatolii.k.hoa.common.domain.CommonException;

import java.time.Year;

public class BudgetException extends CommonException {


    public enum ErrorCode{
        INCORRECT_STATUS,
        BUDGET_ALREADY_FINALIZED,
        CATEGORY_NOT_EXISTS,
        BUDGET_NOT_EXISTS,
        BUDGET_ALREADY_EXISTS,
        NEW_BUDGET_IS_NOT_DRAFT,
        YEAR_IS_REQUIRED,
        CATEGORY_YEAR_MISMATCH,
        CHANGE_STATUS_NOT_ALLOWED
    }

    public static BudgetException newBudgetNotDraft(BudgetPlan.Status status) {
        return new BudgetException(ErrorCode.NEW_BUDGET_IS_NOT_DRAFT.toString(),
                "New budget plan has status [%s], but DRAFT is expected".formatted(status.toString()));
    }

    public static BudgetException alreadyExists(Year year) {
        return new BudgetException(ErrorCode.BUDGET_ALREADY_EXISTS.toString(),
                "Budget plan for %d year already exists".formatted(year.getValue()));
    }


    public static BudgetException notExists(Year year) {
        return new BudgetException(ErrorCode.BUDGET_NOT_EXISTS.toString(),
                "Budget plan for %d year does not exist".formatted(year.getValue()));
    }

    public static BudgetException changeStatusNotAllowed() {
        return new BudgetException(ErrorCode.CHANGE_STATUS_NOT_ALLOWED.toString(),
                "Change of status is not allowed. Use submit/approve operations instead");
    }

    public static BudgetException categoryYearMismatch(Year categoryYear, Year budgetYear) {
        return new BudgetException(ErrorCode.CATEGORY_YEAR_MISMATCH.toString(),
                "Year mismatch. Category year=[%d]. Budget year=[%d]".formatted(categoryYear.getValue(), budgetYear.getValue()));
    }


    public static BudgetException yearIsRequired() {
        return new BudgetException(ErrorCode.YEAR_IS_REQUIRED.toString(), "Year is required");
    }

    public static BudgetException incorrectStatus(BudgetPlan.Status currentStatus, BudgetPlan.Status newStatus ){
        return new BudgetException( ErrorCode.INCORRECT_STATUS.toString(),
                "Unable to change status from [%s] to [%s]".formatted(currentStatus.toString(), newStatus.toString()));
    }

    public static BudgetException budgetAlreadyFinalized() {
        return new BudgetException(ErrorCode.BUDGET_ALREADY_FINALIZED.toString(),
                "Budget plan is finalized already");
    }

    public static BudgetException categoryNotExists(Long id) {
        return new BudgetException(ErrorCode.CATEGORY_NOT_EXISTS.toString(),
                "Category id=[%d] does not exist".formatted(id));
    }

    private BudgetException(String errorCode, String errorDetails) {
        super(errorCode, errorDetails);
    }

}
