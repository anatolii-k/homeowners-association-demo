package anatolii.k.hoa.community.unit.application;

import anatolii.k.hoa.community.resident.api.ResidentService;
import anatolii.k.hoa.community.unit.domain.ResidentServiceClient;
import org.springframework.stereotype.Component;

@Component
public class ResidentServiceClientImpl implements ResidentServiceClient {

    @Override
    public boolean hasResidentsInUnit(Long unitId) {
        return residentService.hasResidentsInUnit(unitId);
    }

    public ResidentServiceClientImpl(ResidentService residentService) {
        this.residentService = residentService;
    }

    private final ResidentService residentService;
}
