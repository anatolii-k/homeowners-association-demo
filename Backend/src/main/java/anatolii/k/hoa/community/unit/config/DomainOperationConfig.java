package anatolii.k.hoa.community.unit.config;

import anatolii.k.hoa.community.unit.domain.DeregisterUnitOperation;
import anatolii.k.hoa.community.unit.domain.ResidentServiceClient;
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
                                                    ResidentServiceClient residentServiceClient){
        return new DeregisterUnitOperation( unitRepository, residentServiceClient);
    }

}
