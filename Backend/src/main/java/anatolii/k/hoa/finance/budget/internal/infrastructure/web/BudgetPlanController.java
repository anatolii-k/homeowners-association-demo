package anatolii.k.hoa.finance.budget.internal.infrastructure.web;

import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.application.*;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Year;
import java.util.Optional;

@RestController
@RequestMapping("/api/budget-plan")
public class BudgetPlanController {

    @GetMapping("{year}")
    ResponseEntity<BudgetPlanDTO> getBudgetPlan(@PathVariable Integer year){
        Optional<BudgetPlanDTO> budgetPlan = budgetPlanRepository.getBudgetPlanForYear(Year.of(year))
                .map(BudgetPlanDTO::fromDomain);
        if( budgetPlan.isPresent() ){
            return ResponseEntity.ok(budgetPlan.get());
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    ResponseEntity<UseCaseResponse<Void>> createBudgetPlan(
            @RequestBody BudgetPlanDTO budgetPlanDTO, UriComponentsBuilder uriBuilder){

        Integer year = budgetPlanDTO.getYear();
        UseCaseResponse<Void> response = createBudgetPlanUseCase.create(budgetPlanDTO);

        if(!response.ok()){
            return ResponseEntity.unprocessableEntity().body(response);
        }

        URI budgetLocation = uriBuilder.path("/api/budget-plan/{year}")
                .buildAndExpand(year)
                .toUri();
        return ResponseEntity.created(budgetLocation).build();
    }

    @PostMapping("{year}/submit")
    ResponseEntity<UseCaseResponse<Void>> submitBudgetPlan( @PathVariable Integer year ){
        UseCaseResponse<Void> response = submitBudgetPlanUseCase.submit(Year.of(year));
        if(response.ok()){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.unprocessableEntity().body(response);
    }

    @PostMapping("{year}/approve")
    ResponseEntity<UseCaseResponse<Void>> approveBudgetPlan( @PathVariable Integer year ){
        UseCaseResponse<Void> response = approveBudgetPlanUseCase.approve(Year.of(year));
        if(response.ok()){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.unprocessableEntity().body(response);
    }

    @PostMapping("{year}/reject")
    ResponseEntity<UseCaseResponse<Void>> rejectBudgetPlan(@PathVariable Integer year ){
        UseCaseResponse<Void> response = rejectBudgetPlanUseCase.reject(Year.of(year));
        if(response.ok()){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.unprocessableEntity().body(response);
    }

    @PutMapping("{year}/category")
    ResponseEntity<UseCaseResponse<Void>> addOrUpdateCategory(
            @PathVariable Integer year, @RequestBody BudgetCategoryDTO categoryDTO){
        UseCaseResponse<Void> response = addOrUpdateBudgetCategoryUseCase.addOrUpdate(year, categoryDTO);
        if(response.ok()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.unprocessableEntity().body(response);
    }

    @DeleteMapping("{year}/category/{id}")
    ResponseEntity<UseCaseResponse<Void>> deleteCategory(
            @PathVariable Integer year, @PathVariable Long id){
        UseCaseResponse<Void> response = deleteBudgetCategoryUseCase.delete(year, id);
        if(response.ok()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.unprocessableEntity().body(response);
    }

    public BudgetPlanController(BudgetPlanRepository budgetPlanRepository, CreateBudgetPlanUseCase createBudgetPlanUseCase, ApproveBudgetPlanUseCase approveBudgetPlanUseCase, SubmitBudgetPlanUseCase submitBudgetPlanUseCase, RejectBudgetPlanUseCase rejectBudgetPlanUseCase, AddOrUpdateBudgetCategoryUseCase addOrUpdateBudgetCategoryUseCase, DeleteBudgetCategoryUseCase deleteBudgetCategoryUseCase) {
        this.budgetPlanRepository = budgetPlanRepository;
        this.createBudgetPlanUseCase = createBudgetPlanUseCase;
        this.approveBudgetPlanUseCase = approveBudgetPlanUseCase;
        this.submitBudgetPlanUseCase = submitBudgetPlanUseCase;
        this.rejectBudgetPlanUseCase = rejectBudgetPlanUseCase;
        this.addOrUpdateBudgetCategoryUseCase = addOrUpdateBudgetCategoryUseCase;
        this.deleteBudgetCategoryUseCase = deleteBudgetCategoryUseCase;
    }

    private final BudgetPlanRepository budgetPlanRepository;
    private final CreateBudgetPlanUseCase createBudgetPlanUseCase;
    private final ApproveBudgetPlanUseCase approveBudgetPlanUseCase;
    private final SubmitBudgetPlanUseCase submitBudgetPlanUseCase;
    private final RejectBudgetPlanUseCase rejectBudgetPlanUseCase;
    private final AddOrUpdateBudgetCategoryUseCase addOrUpdateBudgetCategoryUseCase;
    private final DeleteBudgetCategoryUseCase deleteBudgetCategoryUseCase;
}
