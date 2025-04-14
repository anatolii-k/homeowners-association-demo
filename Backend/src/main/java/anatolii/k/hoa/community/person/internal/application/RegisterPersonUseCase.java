package anatolii.k.hoa.community.person.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.common.domain.Email;
import anatolii.k.hoa.common.domain.PhoneNumber;
import anatolii.k.hoa.common.domain.SSN;
import anatolii.k.hoa.community.person.internal.domain.*;

@UseCase
public class RegisterPersonUseCase {

    public UseCaseResponse<Person> register(PersonDTO personData) {
        return UseCaseProcessor.process(
                ()-> registerImpl(personData)
        );
    }

    private Person registerImpl(PersonDTO personData ){

        checkRequiredAttributes(personData);

        SSN ssn = SSN.fromString(personData.getSsn());
        if(personRepository.existsPersonWithSSN(ssn.toString()) ){
            throw PersonException.ssnAlreadyExists(ssn.toString());
        }

        PhoneNumber phoneNumber = PhoneNumber.fromString(personData.getPhoneNumber());
        Email email = Email.fromString( personData.getEmail() );

        Person newPerson = new Person(null,
                personData.getFirstName(),
                personData.getLastName(),
                phoneNumber, email, ssn);

        return personRepository.save(newPerson);
    }

    private void checkRequiredAttributes(PersonDTO personData){
        checkRequiredAttribute(personData.getSsn(), PersonAttributes.SSN);
        checkRequiredAttribute(personData.getFirstName(), PersonAttributes.FIRST_NAME);
        checkRequiredAttribute(personData.getLastName(), PersonAttributes.LAST_NAME);
        checkRequiredAttribute(personData.getPhoneNumber(), PersonAttributes.PHONE);
    }

    private void checkRequiredAttribute(String value, PersonAttributes attribute) {
        if( value == null || value.isBlank() ){
            throw PersonException.attributeRequired(attribute);
        }
    }


    public RegisterPersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private final PersonRepository personRepository;
}
