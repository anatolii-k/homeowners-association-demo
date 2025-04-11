package anatolii.k.hoa.community.unit.domain;

public class DeregisterUnitOperation {

    public void deregister(Long id ){
        if(!unitRepository.doesUnitExist(id)){
            throw UnitException.notExists(id);
        }
        if(residentServiceClient.hasResidentsInUnit(id)){
            throw UnitException.unitHasResidents(id);
        }
        unitRepository.delete( id );
    }

    public DeregisterUnitOperation(UnitRepository unitRepository, ResidentServiceClient residentServiceClient) {
        this.unitRepository = unitRepository;
        this.residentServiceClient = residentServiceClient;
    }

    private final UnitRepository unitRepository;
    private final ResidentServiceClient residentServiceClient;
}
