package anatolii.k.hoa.finance.budget.internal.domain;

import anatolii.k.hoa.common.domain.MoneyAmount;

import java.time.Year;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BudgetPlan {

    public enum Status{
        DRAFT,
        SUBMITTED,
        APPROVED
    }

    private final Year year;
    private final List<BudgetCategory> categories;
    private Status status = Status.DRAFT;


    public static BudgetPlan create(Year year, List<BudgetCategory> categories, Status status){
        if(year == null){
            throw BudgetException.yearIsRequired();
        }
        if(categories == null){
            categories = new LinkedList<>();
        }

        categories.forEach(category -> validateCategory(year,category));

        return new BudgetPlan( year, categories,
                Optional.ofNullable(status).orElse(Status.DRAFT));
    }

    private BudgetPlan(Year year, List<BudgetCategory> categories, Status status) {
        this.year = year;
        this.categories = categories;
        this.status = status;
    }

    public void submit(){
        if(isDraft()){
            status = Status.SUBMITTED;
        }
        else{
            throw BudgetException.incorrectStatus(status, Status.SUBMITTED);
        }
    }

    public void approve(){
        if(isSubmitted()){
            status = Status.APPROVED;
        }
        else{
            throw BudgetException.incorrectStatus(status, Status.APPROVED);
        }
    }

    public void reject(){
        if(!isApproved()){
            status = Status.DRAFT;
        }
        else {
            throw BudgetException.budgetAlreadyFinalized();
        }
    }

    public void addOrUpdateCategory(BudgetCategory category){
        if( !isDraft() ){
            throw BudgetException.budgetAlreadyFinalized();
        }
        validateCategory(category);

        if( category.getId() == null ){
            categories.add(category);
        }
        else{
            updateCategory(category);
        }
    }

    public void deleteCategory(Long categoryId) {
        if( !isDraft() ){
            throw BudgetException.budgetAlreadyFinalized();
        }
        boolean removed = categories.removeIf( category -> category.getId().equals(categoryId) );
        if(!removed){
            throw BudgetException.categoryNotExists(categoryId);
        }
    }


    public MoneyAmount getTotal(){
        return categories.stream()
                .map(BudgetCategory::getPlannedAmount)
                .reduce( MoneyAmount.zero(), MoneyAmount::add );
    }

    public Year getYear() {
        return year;
    }

    public List<BudgetCategory> getCategories() {
        return categories;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isFinalized(){
        return isSubmitted() || isApproved();
    }

    public boolean isDraft(){
        return status == Status.DRAFT;
    }

    public boolean isSubmitted(){
        return status == Status.SUBMITTED;
    }

    public boolean isApproved(){
        return status == Status.APPROVED;
    }

    private void updateCategory(BudgetCategory category) {
        for( int i = 0; i < categories.size(); ++i ){
            if( categories.get(i).getId().equals(category.getId()) ){
                categories.set(i, category);
                return;
            }
        }
        throw BudgetException.categoryNotExists(category.getId());
    }

    private static void validateCategory(Year budgetYear, BudgetCategory category){
        if(!category.getYear().equals(budgetYear)){
            throw BudgetException.categoryYearMismatch( category.getYear(), budgetYear );
        }
    }

    private void validateCategory(BudgetCategory category){
        validateCategory(year, category);
    }

}
