package anatolii.k.hoa.finance.budget.internal.infrastructure.web;

import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.application.BudgetPlanDTO;
import anatolii.k.hoa.finance.budget.internal.application.BudgetPlanRepository;
import anatolii.k.hoa.finance.budget.internal.application.ChangeBudgetStateUseCases;
import anatolii.k.hoa.finance.budget.internal.application.CreateOrUpdateBudgetPlanUseCase;
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

    @PutMapping
    ResponseEntity<UseCaseResponse<CreateOrUpdateBudgetPlanUseCase.BudgetUpdateResult>>
    createOrUpdateBudgetPlan(@RequestBody BudgetPlanDTO budgetPlanDTO, UriComponentsBuilder uriBuilder){

        Integer year = budgetPlanDTO.getYear();
        UseCaseResponse<CreateOrUpdateBudgetPlanUseCase.BudgetUpdateResult> response
                = createOrUpdateBudgetPlanUseCase.createOrUpdate(budgetPlanDTO);

        if(!response.ok()){
            return ResponseEntity.unprocessableEntity().body(response);
        }
        if(response.data() == CreateOrUpdateBudgetPlanUseCase.BudgetUpdateResult.UPDATED){
            return ResponseEntity.noContent().build();
        }

        assert (response.data() == CreateOrUpdateBudgetPlanUseCase.BudgetUpdateResult.CREATED);

        URI budgetLocation = uriBuilder.path("/api/budget-plan/{year}")
                .buildAndExpand(year)
                .toUri();
        return ResponseEntity.created(budgetLocation).build();
    }

    @PostMapping("{year}/{action}")
    ResponseEntity<UseCaseResponse<Void>> changeBudgetState( @PathVariable Integer year, @PathVariable String action ){
        UseCaseResponse<Void> response = changeBudgetStateUseCases.changeState(year, action);
        if(response.ok()){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.unprocessableEntity().body(response);
    }

    public BudgetPlanController(BudgetPlanRepository budgetPlanRepository, CreateOrUpdateBudgetPlanUseCase createOrUpdateBudgetPlanUseCase, ChangeBudgetStateUseCases changeBudgetStateUseCases) {
        this.budgetPlanRepository = budgetPlanRepository;
        this.createOrUpdateBudgetPlanUseCase = createOrUpdateBudgetPlanUseCase;
        this.changeBudgetStateUseCases = changeBudgetStateUseCases;
    }

    private final BudgetPlanRepository budgetPlanRepository;
    private final CreateOrUpdateBudgetPlanUseCase createOrUpdateBudgetPlanUseCase;
    private final ChangeBudgetStateUseCases changeBudgetStateUseCases;
}
