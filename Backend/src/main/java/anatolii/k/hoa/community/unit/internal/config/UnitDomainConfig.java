package anatolii.k.hoa.community.unit.internal.config;

import anatolii.k.hoa.community.unit.internal.domain.DeregisterUnitOperation;
import anatolii.k.hoa.community.unit.internal.domain.RegisterUnitOperation;
import anatolii.k.hoa.community.unit.internal.domain.UnitRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnitDomainConfig {
    @Bean
    RegisterUnitOperation registerUnitOperation(UnitRepository unitRepository){
        return new RegisterUnitOperation( unitRepository );
    }

    @Bean
    DeregisterUnitOperation deregisterUnitOperation(UnitRepository unitRepository){
        return new DeregisterUnitOperation( unitRepository);
    }

}
