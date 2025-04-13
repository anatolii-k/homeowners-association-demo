package anatolii.k.hoa.community.unit;

import anatolii.k.hoa.community.unit.internal.domain.UnitRepository;
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
