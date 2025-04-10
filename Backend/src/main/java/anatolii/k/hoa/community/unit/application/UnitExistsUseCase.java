package anatolii.k.hoa.community.unit.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.community.unit.domain.UnitRepository;

@UseCase
public class UnitExistsUseCase {

    public boolean doesUnitExist(Long id){
        return unitRepository.doesUnitExist(id);
    }

    public UnitExistsUseCase(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    private final UnitRepository unitRepository;
}
