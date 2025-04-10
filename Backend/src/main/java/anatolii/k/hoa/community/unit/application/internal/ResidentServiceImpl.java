package anatolii.k.hoa.community.unit.application.internal;

import anatolii.k.hoa.community.resident.application.ResidentExistsUseCase;
import anatolii.k.hoa.community.unit.domain.ResidentService;
import org.springframework.stereotype.Service;

@Service
public class ResidentServiceImpl implements ResidentService {

    @Override
    public boolean hasResidentsInUnit(Long unitId) {
        return residentExistsUseCase.hasResidentsInUnit(unitId);
    }

    public ResidentServiceImpl(ResidentExistsUseCase residentExistsUseCase) {
        this.residentExistsUseCase = residentExistsUseCase;
    }

    private final ResidentExistsUseCase residentExistsUseCase;
}
