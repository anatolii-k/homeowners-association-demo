package anatolii.k.hoa.community.unit.internal.domain;

public class RegisterUnitOperation {

    public Unit register(String unitNumber, Integer unitSquare ) {
        if(unitRepository.doesUnitExist(unitNumber)){
            throw UnitException.alreadyExists(unitNumber);
        }
        return unitRepository.save( new Unit(null, unitNumber, unitSquare) );
    }

    public RegisterUnitOperation(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    private final UnitRepository unitRepository;
}
