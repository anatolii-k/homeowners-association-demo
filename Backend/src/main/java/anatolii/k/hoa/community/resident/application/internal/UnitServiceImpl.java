package anatolii.k.hoa.community.resident.application.internal;

import anatolii.k.hoa.community.resident.domain.UnitService;
import anatolii.k.hoa.community.unit.application.UnitExistsUseCase;
import org.springframework.stereotype.Service;

@Service
public class UnitServiceImpl implements UnitService {
    @Override
    public boolean doesUnitExist(Long id) {
        return unitExistsUseCase.doesUnitExist(id);
    }

    public UnitServiceImpl(UnitExistsUseCase unitExistsUseCase) {
        this.unitExistsUseCase = unitExistsUseCase;
    }

    private final UnitExistsUseCase unitExistsUseCase;
}
