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
        if(!unitService.doesUnitExist(id)){
            throw UnitException.notExists(id);
        }
    }

    public RegisterResidentOperation(UnitService unitService, PersonService personService, ResidentRepository residentRepository) {
        this.unitService = unitService;
        this.personService = personService;
        this.residentRepository = residentRepository;
    }

    private final UnitService unitService;
    private final PersonService personService;
    private final ResidentRepository residentRepository;
}
