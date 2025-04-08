package anatolii.k.hoa.community.units.infrastructure.web;

import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.units.application.GetUnitsUseCases;
import anatolii.k.hoa.community.units.application.UnitRegistrationUseCases;
import anatolii.k.hoa.community.units.domain.Unit;
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
        return ResponseEntity.ok( getUnitsUseCases.getAllUnits() );
    }

    @GetMapping("{id}")
    ResponseEntity<Unit> getUnit( @PathVariable("id") Long unitId ){
        Optional<Unit> unit = getUnitsUseCases.getUnit(unitId);
        if( unit.isEmpty() ){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(unit.get());
    }

    @PostMapping
    ResponseEntity<UseCaseResponse<Unit>> registerNewUnit(@RequestBody Unit unit, UriComponentsBuilder uriBuilder){

        UseCaseResponse<Unit> useCaseResponse = unitRegistrationUseCases.register(unit.number(), unit.square());
        if(useCaseResponse.ok()){
            URI uri = uriBuilder.path("api/units/{id}")
                    .buildAndExpand(useCaseResponse.data().id())
                    .toUri();
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.unprocessableEntity().body(useCaseResponse);
    }


    @DeleteMapping("{id}")
    ResponseEntity<UseCaseResponse<Void>> unregisterUnit( @PathVariable("id") Long unitId ){

        UseCaseResponse<Void> useCaseResponse = unitRegistrationUseCases.unregister(unitId);
        if(useCaseResponse.ok()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.unprocessableEntity().body(useCaseResponse);
    }

    public UnitsController(GetUnitsUseCases getUnitsUseCases, UnitRegistrationUseCases unitRegistrationUseCases) {
        this.getUnitsUseCases = getUnitsUseCases;
        this.unitRegistrationUseCases = unitRegistrationUseCases;
    }

    private final GetUnitsUseCases getUnitsUseCases;
    private final UnitRegistrationUseCases unitRegistrationUseCases;
}
