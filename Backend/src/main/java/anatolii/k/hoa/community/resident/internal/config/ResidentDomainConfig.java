package anatolii.k.hoa.community.resident.internal.config;

import anatolii.k.hoa.community.resident.internal.domain.PersonServiceClient;
import anatolii.k.hoa.community.resident.internal.domain.ResidentRepository;
import anatolii.k.hoa.community.resident.internal.domain.RegisterResidentOperation;
import anatolii.k.hoa.community.resident.internal.domain.UnitServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResidentDomainConfig {

    @Bean
    RegisterResidentOperation registerResidentOperation(UnitServiceClient unitServiceClient, PersonServiceClient personServiceClient, ResidentRepository residentRepository){
        return new RegisterResidentOperation(unitServiceClient, personServiceClient, residentRepository);
    }
}
