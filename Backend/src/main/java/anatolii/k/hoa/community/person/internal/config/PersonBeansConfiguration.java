package anatolii.k.hoa.community.person.internal.config;

import anatolii.k.hoa.community.person.internal.application.PersonAttributesValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonBeansConfiguration {

    @Bean
    PersonAttributesValidation personAttributesValidation( PersonRequiredAttributesReader reader ){
        return new PersonAttributesValidation( reader.getRequiredAttributes() );
    }
}
