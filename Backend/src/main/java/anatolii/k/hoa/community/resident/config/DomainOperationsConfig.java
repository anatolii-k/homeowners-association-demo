package anatolii.k.hoa.community.resident.config;

import anatolii.k.hoa.community.resident.domain.PersonService;
import anatolii.k.hoa.community.resident.domain.ResidentRepository;
import anatolii.k.hoa.community.resident.domain.RegisterResidentOperation;
import anatolii.k.hoa.community.resident.domain.UnitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainOperationsConfig {

    @Bean
    RegisterResidentOperation registerResidentOperation(UnitService unitService, PersonService personService, ResidentRepository residentRepository){
        return new RegisterResidentOperation(unitService, personService, residentRepository);
    }
}
