package anatolii.k.hoa.community.members.domain;

import anatolii.k.hoa.common.domain.Email;
import anatolii.k.hoa.common.domain.PhoneNumber;
import anatolii.k.hoa.common.domain.SSN;

public class RegisterNewPersonOperation {

    public RegisterNewPersonOperation(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person register(String firstName,
                    String lastName,
                    String phoneNumberString,
                    String emailString,
                    String ssnString ){

        checkRequiredAttribute(ssnString, PersonAttributes.SSN);
        SSN ssn = getSSN(ssnString);
        if(personRepository.existsPersonWithSSN(ssn.toString()) ){
            throw PersonException.ssnAlreadyExists(ssn.toString());
        }

        checkRequiredAttribute(firstName, PersonAttributes.FIRST_NAME);
        checkRequiredAttribute(lastName, PersonAttributes.LAST_NAME);
        checkRequiredAttribute(phoneNumberString, PersonAttributes.PHONE);

        PhoneNumber phoneNumber = getPhoneNumber(phoneNumberString);
        Email email = getEmail(emailString);

        Person newPerson = new Person(null, firstName, lastName, phoneNumber, email, ssn);

        return personRepository.save(newPerson);
    }

    private Email getEmail(String emailString) {
        if(emailString == null || emailString.isBlank()){
            return null;
        }
        try{
            return Email.fromString(emailString);
        }
        catch (Throwable ex){
            throw PersonException.invalidAttributeValue(PersonAttributes.EMAIL, emailString, ex.getMessage());
        }
    }

    private PhoneNumber getPhoneNumber(String phoneNumberString) {
        try{
            return PhoneNumber.fromString(phoneNumberString);
        }
        catch (Throwable ex){
            throw PersonException.invalidAttributeValue(PersonAttributes.PHONE, phoneNumberString, ex.getMessage());
        }
    }

    private SSN getSSN(String ssnString) {
        try{
            return SSN.fromString(ssnString);
        }
        catch (Throwable ex){
            throw PersonException.invalidAttributeValue(PersonAttributes.SSN, ssnString, ex.getMessage());
        }
    }

    private void checkRequiredAttribute(String value, PersonAttributes attribute) {
        if( value == null || value.isBlank() ){
            throw PersonException.attributeRequired(attribute);
        }
    }

    private final PersonRepository personRepository;
}
