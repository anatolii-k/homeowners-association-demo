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

        attributesValidation.validate(personData);

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

    public RegisterPersonUseCase(PersonAttributesValidation attributesValidation, PersonRepository personRepository) {
        this.attributesValidation = attributesValidation;
        this.personRepository = personRepository;
    }

    private final PersonAttributesValidation attributesValidation;
    private final PersonRepository personRepository;
}
