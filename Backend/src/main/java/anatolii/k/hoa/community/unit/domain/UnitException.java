package anatolii.k.hoa.community.unit.domain;

import anatolii.k.hoa.common.domain.CommonException;

public class UnitException extends CommonException {

    public enum ErrorCode{
        ALREADY_EXISTS,
        NOT_EXISTS,
        HAS_RESIDENTS
    }

    public static UnitException alreadyExists(String unitNumber) {
        return new UnitException(ErrorCode.ALREADY_EXISTS,
                "Unit number=[%s] already exists".formatted(unitNumber));
    }

    public static UnitException notExists(Long id) {
        return new UnitException(ErrorCode.NOT_EXISTS,
                "Unit id=[%d] does not exist".formatted(id));
    }

    public static UnitException unitHasResidents(Long id) {
        return new UnitException(ErrorCode.HAS_RESIDENTS,
                "Unit id=[%d] has resident(s)".formatted(id));
    }

    private UnitException(ErrorCode errorCode, String errorDetails) {
        super(errorCode.toString(), errorDetails);
    }
}
