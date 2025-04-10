package anatolii.k.hoa.community.unit.config;

import anatolii.k.hoa.community.unit.domain.ResidentService;
import anatolii.k.hoa.community.unit.domain.UnitRegistrationOperations;
import anatolii.k.hoa.community.unit.domain.UnitRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainOperationConfig {
    @Bean
    UnitRegistrationOperations unitRegistrationOperations(UnitRepository unitRepository,
                                                          ResidentService residentService){
        return new UnitRegistrationOperations( unitRepository, residentService);
    }
}
