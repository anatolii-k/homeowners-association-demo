package anatolii.k.hoa.community.unit.internal.application;

import anatolii.k.hoa.common.domain.CommonException;

public class UnitException extends CommonException {

    public enum ErrorCode{
        ALREADY_EXISTS,
        NOT_EXISTS
    }

    public static UnitException alreadyExists(String unitNumber) {
        return new UnitException(ErrorCode.ALREADY_EXISTS,
                "Unit number=[%s] already exists".formatted(unitNumber));
    }

    public static UnitException notExists(Long id) {
        return new UnitException(ErrorCode.NOT_EXISTS,
                "Unit id=[%d] does not exist".formatted(id));
    }

    private UnitException(ErrorCode errorCode, String errorDetails) {
        super(errorCode.toString(), errorDetails);
    }
}
