package anatolii.k.hoa.community.resident.internal.infrastructure.web;

import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.resident.internal.application.RegisterResidentUseCase;
import anatolii.k.hoa.community.resident.internal.domain.ResidentRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/resident")
public class ResidentController {

    @PostMapping
    ResponseEntity<UseCaseResponse<ResidentRecord>> registerResident(@RequestBody ResidentRecord residentRecord,
                                                           UriComponentsBuilder uriComponentsBuilder){
        UseCaseResponse<ResidentRecord> useCaseResponse = registerResidentUseCase.registerResident(residentRecord);
        if(useCaseResponse.ok()){
            Long unitId = useCaseResponse.data().getUnitId();
            URI uri = uriComponentsBuilder.path("/api/resident/unit/{id}")
                    .buildAndExpand(unitId)
                    .toUri();
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.unprocessableEntity().body(useCaseResponse);
    }

    public ResidentController(RegisterResidentUseCase registerResidentUseCase) {
        this.registerResidentUseCase = registerResidentUseCase;
    }

    private final RegisterResidentUseCase registerResidentUseCase;
}
