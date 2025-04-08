package anatolii.k.hoa.community.units.domain;

import java.util.Optional;

public class UnitException extends RuntimeException{

    public enum ErrorCode{
        ALREADY_EXISTS,
        NOT_EXISTS,
        HAS_RESIDENTS
    }

    public static UnitException alreadyExists(String unitNumber) {
        return new UnitException("Unit number=[%s] already exists".formatted(unitNumber),
                ErrorCode.ALREADY_EXISTS, null, unitNumber);
    }

    public static UnitException notExists(Long id) {
        return new UnitException("Unit id=[%d] does not exist".formatted(id),
                ErrorCode.NOT_EXISTS, id, null);
    }

    public static UnitException unitHasResidents(Long id) {
        return new UnitException("Unit id=[%d] has resident(s) assigned".formatted(id),
                ErrorCode.HAS_RESIDENTS, id, null);
    }


    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Optional<Long> getUnitId() {
        return Optional.ofNullable(unitId);
    }

    public Optional<String> getUnitNumber() {
        return Optional.ofNullable(unitNumber);
    }

    private UnitException(String message, ErrorCode errorCode, Long unitId, String unitNumber) {
        super(message);
        this.errorCode = errorCode;
        this.unitId = unitId;
        this.unitNumber = unitNumber;
    }

    private final ErrorCode errorCode;
    private final Long unitId;
    private final String unitNumber;
}
