package anatolii.k.hoa.community.unit.domain;

import anatolii.k.hoa.common.domain.CommonException;

public class UnitException extends CommonException {

    public enum ErrorCode{
        ALREADY_EXISTS,
        NOT_EXISTS,
        HAS_RESIDENTS
    }

    public static UnitException alreadyExists(String unitNumber) {
        return new UnitException("Unit number=[%s] already exists".formatted(unitNumber),
                ErrorCode.ALREADY_EXISTS);
    }

    public static UnitException notExists(Long id) {
        return new UnitException("Unit id=[%d] does not exist".formatted(id),
                ErrorCode.NOT_EXISTS);
    }

    public static UnitException unitHasResidents(Long id) {
        return new UnitException("Unit id=[%d] has resident(s) assigned".formatted(id),
                ErrorCode.HAS_RESIDENTS);
    }


    private UnitException(String errorDetails, ErrorCode errorCode) {
        super(errorCode.toString(), errorDetails);
    }
}
