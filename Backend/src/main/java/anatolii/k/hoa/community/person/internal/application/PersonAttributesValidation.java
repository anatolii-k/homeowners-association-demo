package anatolii.k.hoa.community.person.internal.application;

import anatolii.k.hoa.common.application.AttributeValidators;
import anatolii.k.hoa.common.application.AttributesValidation;

import java.util.EnumSet;

public class PersonAttributesValidation {

    public void validate(PersonDTO personData){
        validator.validate(PersonAttributes.FIRST_NAME.toString(), personData.getFirstName());
        validator.validate(PersonAttributes.LAST_NAME.toString(), personData.getLastName());
        validator.validate(PersonAttributes.PHONE.toString(), personData.getPhoneNumber());
        validator.validate(PersonAttributes.EMAIL.toString(), personData.getEmail());
        validator.validate(PersonAttributes.SSN.toString(), personData.getSsn());
    }

    public PersonAttributesValidation(EnumSet<PersonAttributes> requiredAttributes) {
        requiredAttributes.forEach(
                attr -> validator.registerValidator(attr.toString(), AttributeValidators.notEmpty())
        );
    }

    private final AttributesValidation validator = new AttributesValidation();
}
