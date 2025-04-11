package anatolii.k.hoa.community.unit.config;

import anatolii.k.hoa.community.unit.domain.DeregisterUnitOperation;
import anatolii.k.hoa.community.unit.domain.ResidentService;
import anatolii.k.hoa.community.unit.domain.RegisterUnitOperation;
import anatolii.k.hoa.community.unit.domain.UnitRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainOperationConfig {
    @Bean
    RegisterUnitOperation registerUnitOperation(UnitRepository unitRepository){
        return new RegisterUnitOperation( unitRepository );
    }

    @Bean
    DeregisterUnitOperation deregisterUnitOperation(UnitRepository unitRepository,
                                                    ResidentService residentService){
        return new DeregisterUnitOperation( unitRepository, residentService );
    }

}
