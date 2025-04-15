package anatolii.k.hoa.community.resident.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.person.PersonService;
import anatolii.k.hoa.community.resident.internal.domain.*;
import anatolii.k.hoa.community.unit.UnitService;

import java.time.LocalDate;

@UseCase
public class RegisterResidentUseCase {

    public UseCaseResponse<ResidentRecord> register(ResidentRecord residentRecord){
        return UseCaseProcessor.process( () -> registerImpl(residentRecord) );
    }

    private ResidentRecord registerImpl(ResidentRecord residentRecord){

        unitService.checkUnitExists(residentRecord.getUnitId());
        personService.checkPersonExists(residentRecord.getPersonId());

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

    public RegisterResidentUseCase(UnitService unitService, PersonService personService, ResidentRepository residentRepository) {
        this.unitService = unitService;
        this.personService = personService;
        this.residentRepository = residentRepository;
    }

    private final UnitService unitService;
    private final PersonService personService;
    private final ResidentRepository residentRepository;
}
