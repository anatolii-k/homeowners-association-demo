package anatolii.k.hoa.community.unit.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.community.unit.domain.Unit;
import anatolii.k.hoa.community.unit.domain.UnitRepository;

import java.util.List;
import java.util.Optional;

@UseCase
public class GetUnitsUseCases {

    public List<Unit> getAllUnits() {
        return unitRepository.getAllUnits();
    }

    public Optional<Unit> getUnit(Long unitId) {
        return unitRepository.getUnitById(unitId);
    }

    public GetUnitsUseCases(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    private final UnitRepository unitRepository;

}
