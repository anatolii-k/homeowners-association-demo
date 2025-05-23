package anatolii.k.hoa.community.person.internal.application;

import anatolii.k.hoa.common.domain.CommonException;

public class PersonException extends CommonException {

    public enum ErrorCode{
        SSN_ALREADY_EXISTS,
        NOT_EXISTS
    }

    public static PersonException notExists(Long id) {
        return new PersonException(ErrorCode.NOT_EXISTS.toString(),
                "Person with id=[%d] does not exist".formatted(id));
    }

    public static PersonException ssnAlreadyExists(String ssn){
        return new PersonException(ErrorCode.SSN_ALREADY_EXISTS.toString(),
                "Person with SSN [%s] already exists".formatted(ssn));
    }

    private PersonException(String errorCode, String errorDetails) {
        super(errorCode, errorDetails);
    }
}
