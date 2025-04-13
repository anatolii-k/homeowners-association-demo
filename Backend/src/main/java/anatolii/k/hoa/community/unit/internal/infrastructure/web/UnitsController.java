package anatolii.k.hoa.community.unit.internal.infrastructure.web;

import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.unit.internal.application.DeregisterUnitUseCase;
import anatolii.k.hoa.community.unit.internal.application.RegisterUnitUseCase;
import anatolii.k.hoa.community.unit.internal.domain.Unit;
import anatolii.k.hoa.community.unit.internal.application.UnitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/units")
public class UnitsController {

    @GetMapping
    ResponseEntity<List<Unit>> getAllUnits(){
        return ResponseEntity.ok( unitRepository.getAllUnits() );
    }

    @GetMapping("{id}")
    ResponseEntity<Unit> getUnit( @PathVariable("id") Long unitId ){
        Optional<Unit> unit = unitRepository.getUnitById(unitId);
        if( unit.isEmpty() ){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(unit.get());
    }

    @PostMapping
    ResponseEntity<UseCaseResponse<Unit>> registerNewUnit(@RequestBody Unit unit, UriComponentsBuilder uriBuilder){

        UseCaseResponse<Unit> useCaseResponse = registerUnitUseCase.register(unit.number(), unit.area());
        if(useCaseResponse.ok()){
            URI uri = uriBuilder.path("api/units/{id}")
                    .buildAndExpand(useCaseResponse.data().id())
                    .toUri();
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.unprocessableEntity().body(useCaseResponse);
    }


    @DeleteMapping("{id}")
    ResponseEntity<UseCaseResponse<Void>> deregisterUnit(@PathVariable("id") Long unitId ){

        UseCaseResponse<Void> useCaseResponse = deregisterUnitUseCase.deregister(unitId);
        if(useCaseResponse.ok()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.unprocessableEntity().body(useCaseResponse);
    }

    public UnitsController(UnitRepository unitRepository, RegisterUnitUseCase registerUnitUseCase, DeregisterUnitUseCase deregisterUnitUseCase) {
        this.unitRepository = unitRepository;
        this.registerUnitUseCase = registerUnitUseCase;
        this.deregisterUnitUseCase = deregisterUnitUseCase;
    }

    private final UnitRepository unitRepository;
    private final RegisterUnitUseCase registerUnitUseCase;
    private final DeregisterUnitUseCase deregisterUnitUseCase;
}
