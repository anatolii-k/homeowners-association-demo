package anatolii.k.hoa.community.person.internal.domain;

import anatolii.k.hoa.common.domain.CommonException;

public class PersonException extends CommonException {

    public enum ErrorCode{
        REQUIRED,  // error code is constructed. E.g: PersonAttribute.PHONE + '_' + 'REQUIRED'
        INVALID,   // similar to REQUIRED
        SSN_ALREADY_EXISTS,
        NOT_EXISTS
    }

    public static PersonException notExists(Long id) {
        return new PersonException(ErrorCode.NOT_EXISTS.toString(),
                "Person with id=[%d] does not exist".formatted(id));
    }

    public static PersonException attributeRequired(PersonAttributes attribute){
        String attributeName = attribute.toString();
        return new PersonException( attributeName + "_" + ErrorCode.REQUIRED,
                attributeName + " is required");
    }

    public static PersonException invalidAttributeValue(PersonAttributes attribute, String value, String details){
        String attributeName = attribute.toString();
        return new PersonException( attributeName + "_" + ErrorCode.INVALID,
                "%s has invalid value=[%s]. Details: %s".formatted(attributeName, value, details));
    }

    static PersonException ssnAlreadyExists(String ssn){
        return new PersonException(ErrorCode.SSN_ALREADY_EXISTS.toString(),
                "Person with SSN [%s] already exists".formatted(ssn));
    }

    private PersonException(String errorCode, String errorDetails) {
        super(errorCode, errorDetails);
    }
}
