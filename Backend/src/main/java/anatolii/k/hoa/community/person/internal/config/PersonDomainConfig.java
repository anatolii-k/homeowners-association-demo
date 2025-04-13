package anatolii.k.hoa.community.person.internal.config;

import anatolii.k.hoa.community.person.internal.domain.PersonRepository;
import anatolii.k.hoa.community.person.internal.domain.RegisterPersonOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonDomainConfig {

    @Bean
    RegisterPersonOperation registerPersonOperation(PersonRepository personRepository){
        return new RegisterPersonOperation(personRepository);
    }
}
