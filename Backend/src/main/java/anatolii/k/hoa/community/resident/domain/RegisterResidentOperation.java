package anatolii.k.hoa.community.resident.domain;

import anatolii.k.hoa.community.person.domain.PersonException;
import anatolii.k.hoa.community.unit.domain.UnitException;

import java.time.LocalDate;
import java.util.Optional;

public class RegisterResidentOperation {

    public ResidentRecord register(ResidentRecord residentRecord){
        var unitId = Optional.ofNullable(residentRecord.getUnitId());
        unitId.ifPresentOrElse(
                this::checkThatUnitExists,
                () -> { throw UnitException.notExists(0L); }
        );

        var personId = Optional.ofNullable(residentRecord.getPersonId());
        personId.ifPresentOrElse(
                this::checkThatPersonExists,
                () -> { throw PersonException.notExists(0L);}
        );

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

    private void checkThatPersonExists(Long id) {
        if(!personService.doesPersonExist(id)){
            throw PersonException.notExists(id);
        }
    }

    private void checkThatUnitExists(Long id){
        if(!unitServiceClient.doesUnitExist(id)){
            throw UnitException.notExists(id);
        }
    }

    public RegisterResidentOperation(UnitServiceClient unitServiceClient, PersonService personService, ResidentRepository residentRepository) {
        this.unitServiceClient = unitServiceClient;
        this.personService = personService;
        this.residentRepository = residentRepository;
    }

    private final UnitServiceClient unitServiceClient;
    private final PersonService personService;
    private final ResidentRepository residentRepository;
}
