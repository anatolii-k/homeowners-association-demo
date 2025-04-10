package anatolii.k.hoa.community.unit.domain;

public class UnitRegistrationOperations {

    public Unit register(String unitNumber, Integer unitSquare ) {
        if(unitRepository.doesUnitExist(unitNumber)){
            throw UnitException.alreadyExists(unitNumber);
        }
        return unitRepository.save( new Unit(null, unitNumber, unitSquare) );
    }

    public void unregister(Long id ){
        if(!unitRepository.doesUnitExist(id)){
            throw UnitException.notExists(id);
        }
        if(residentService.hasResidentsInUnit(id)){
            throw UnitException.unitHasResidents(id);
        }
        unitRepository.delete( id );
    }

    public UnitRegistrationOperations(UnitRepository unitRepository, ResidentService residentService) {
        this.unitRepository = unitRepository;
        this.residentService = residentService;
    }

    private final UnitRepository unitRepository;
    private final ResidentService residentService;
}
