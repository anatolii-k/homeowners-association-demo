package anatolii.k.hoa.community.person.internal.domain;

import anatolii.k.hoa.common.domain.Email;
import anatolii.k.hoa.common.domain.PhoneNumber;
import anatolii.k.hoa.common.domain.SSN;

public class RegisterPersonOperation {

    public Person register(RegisterPersonRequest registerPersonRequest ){

        checkRequiredAttributes(registerPersonRequest);

        SSN ssn = SSN.fromString(registerPersonRequest.getSsn());
        if(personRepository.existsPersonWithSSN(ssn.toString()) ){
            throw PersonException.ssnAlreadyExists(ssn.toString());
        }

        PhoneNumber phoneNumber = PhoneNumber.fromString(registerPersonRequest.getPhoneNumber());
        Email email = Email.fromString( registerPersonRequest.getEmail() );

        Person newPerson = new Person(null,
                registerPersonRequest.getFirstName(),
                registerPersonRequest.getLastName(),
                phoneNumber, email, ssn);

        return personRepository.save(newPerson);
    }

    private void checkRequiredAttributes(RegisterPersonRequest registerPersonRequest){
        checkRequiredAttribute(registerPersonRequest.getSsn(), PersonAttributes.SSN);
        checkRequiredAttribute(registerPersonRequest.getFirstName(), PersonAttributes.FIRST_NAME);
        checkRequiredAttribute(registerPersonRequest.getLastName(), PersonAttributes.LAST_NAME);
        checkRequiredAttribute(registerPersonRequest.getPhoneNumber(), PersonAttributes.PHONE);
    }

    private void checkRequiredAttribute(String value, PersonAttributes attribute) {
        if( value == null || value.isBlank() ){
            throw PersonException.attributeRequired(attribute);
        }
    }

    public RegisterPersonOperation(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private final PersonRepository personRepository;
}
