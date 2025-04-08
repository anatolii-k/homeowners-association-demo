package anatolii.k.hoa.community.units.domain;

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
        if(unitRepository.hasUnitResidents(id)){
            throw UnitException.unitHasResidents(id);
        }
        unitRepository.delete( id );
    }

    public UnitRegistrationOperations(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    private final UnitRepository unitRepository;
}
