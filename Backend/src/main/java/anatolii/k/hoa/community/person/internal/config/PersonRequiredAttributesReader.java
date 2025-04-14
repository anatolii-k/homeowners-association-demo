package anatolii.k.hoa.community.person.internal.config;

import anatolii.k.hoa.community.person.internal.application.PersonAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

@Component
public class PersonRequiredAttributesReader {

    @Value("${hoa.person.attributes.required}")
    private String requiredAttributesList;

    public EnumSet<PersonAttributes> getRequiredAttributes(){
        return Arrays.stream(requiredAttributesList.split(","))
                .map( PersonAttributes::valueOf )
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(PersonAttributes.class)));
    }
}
