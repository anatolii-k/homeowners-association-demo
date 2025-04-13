package anatolii.k.hoa.community.unit.internal.domain;

public class DeregisterUnitOperation {

    public void deregister(Long id ){
        if(!unitRepository.doesUnitExist(id)){
            throw UnitException.notExists(id);
        }
        unitRepository.delete( id );
    }

    public DeregisterUnitOperation(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    private final UnitRepository unitRepository;
}
