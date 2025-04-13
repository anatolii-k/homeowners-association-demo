package anatolii.k.hoa.community.resident.internal.domain;

import java.time.LocalDate;

public class RegisterResidentOperation {

    public ResidentRecord register(ResidentRecord residentRecord){

        unitServiceClient.checkUnitExists(residentRecord.getUnitId());
        personServiceClient.checkPersonExists(residentRecord.getPersonId());

        LocalDate registeredAt = residentRecord.getRegisteredAt();
        if(registeredAt == null){
            residentRecord.setRegisteredAt(LocalDate.now());
        }
        else{
            checkDate(registeredAt);
        }
        return residentRepository.save(residentRecord);
    }

    private void checkDate(LocalDate registeredAt) {
        if(registeredAt.isAfter(LocalDate.now())){
            throw ResidentException.registredAtInFuture(registeredAt);
        }
    }

    public RegisterResidentOperation(UnitServiceClient unitServiceClient, PersonServiceClient personServiceClient, ResidentRepository residentRepository) {
        this.unitServiceClient = unitServiceClient;
        this.personServiceClient = personServiceClient;
        this.residentRepository = residentRepository;
    }

    private final UnitServiceClient unitServiceClient;
    private final PersonServiceClient personServiceClient;
    private final ResidentRepository residentRepository;
}
