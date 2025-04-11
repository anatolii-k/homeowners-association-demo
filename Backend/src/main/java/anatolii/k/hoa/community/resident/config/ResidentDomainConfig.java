package anatolii.k.hoa.community.resident.config;

import anatolii.k.hoa.community.resident.domain.PersonServiceClient;
import anatolii.k.hoa.community.resident.domain.ResidentRepository;
import anatolii.k.hoa.community.resident.domain.RegisterResidentOperation;
import anatolii.k.hoa.community.resident.domain.UnitServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResidentDomainConfig {

    @Bean
    RegisterResidentOperation registerResidentOperation(UnitServiceClient unitServiceClient, PersonServiceClient personServiceClient, ResidentRepository residentRepository){
        return new RegisterResidentOperation(unitServiceClient, personServiceClient, residentRepository);
    }
}
