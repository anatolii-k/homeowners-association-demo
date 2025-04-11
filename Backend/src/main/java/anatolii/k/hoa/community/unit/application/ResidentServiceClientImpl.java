package anatolii.k.hoa.community.unit.application;

import anatolii.k.hoa.community.resident.application.ResidentExistsUseCase;
import anatolii.k.hoa.community.unit.domain.ResidentServiceClient;
import org.springframework.stereotype.Service;

@Service
public class ResidentServiceClientImpl implements ResidentServiceClient {

    @Override
    public boolean hasResidentsInUnit(Long unitId) {
        return residentExistsUseCase.hasResidentsInUnit(unitId);
    }

    public ResidentServiceClientImpl(ResidentExistsUseCase residentExistsUseCase) {
        this.residentExistsUseCase = residentExistsUseCase;
    }

    private final ResidentExistsUseCase residentExistsUseCase;
}
