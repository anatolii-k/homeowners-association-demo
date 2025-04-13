package anatolii.k.hoa.community.resident.internal.application;

import anatolii.k.hoa.community.resident.internal.domain.UnitServiceClient;
import anatolii.k.hoa.community.unit.UnitService;
import org.springframework.stereotype.Component;

@Component
public class UnitServiceClientImpl implements UnitServiceClient {
    @Override
    public boolean doesUnitExist(Long id) {
        return unitService.doesUnitExist(id);
    }

    public UnitServiceClientImpl(UnitService unitService) {
        this.unitService = unitService;
    }

    private final UnitService unitService;
}
