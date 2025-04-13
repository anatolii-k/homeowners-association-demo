package anatolii.k.hoa.community.unit;

import anatolii.k.hoa.community.unit.internal.application.UnitException;
import anatolii.k.hoa.community.unit.internal.application.UnitRepository;
import org.springframework.stereotype.Service;

@Service
public class UnitService {

    public void checkUnitExists(Long id){
        if(id == null){
            throw UnitException.notExists(0L);
        }
        if(!unitRepository.doesUnitExist(id)){
            throw UnitException.notExists(id);
        }
    }

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    private final UnitRepository unitRepository;
}
