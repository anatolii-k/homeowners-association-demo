package anatolii.k.hoa.community.unit.api;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.community.unit.domain.UnitRepository;
import org.springframework.stereotype.Service;

@Service
public class UnitService {

    public boolean doesUnitExist(Long id){
        return unitRepository.doesUnitExist(id);
    }

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    private final UnitRepository unitRepository;
}
