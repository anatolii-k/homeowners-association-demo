package anatolii.k.hoa.community.unit.domain;

public class DeregisterUnitOperation {

    public void deregister(Long id ){
        if(!unitRepository.doesUnitExist(id)){
            throw UnitException.notExists(id);
        }
        if(residentService.hasResidentsInUnit(id)){
            throw UnitException.unitHasResidents(id);
        }
        unitRepository.delete( id );
    }

    public DeregisterUnitOperation(UnitRepository unitRepository, ResidentService residentService) {
        this.unitRepository = unitRepository;
        this.residentService = residentService;
    }

    private final UnitRepository unitRepository;
    private final ResidentService residentService;
}
