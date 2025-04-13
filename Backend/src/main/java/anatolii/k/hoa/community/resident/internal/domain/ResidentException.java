package anatolii.k.hoa.community.resident.internal.domain;

import anatolii.k.hoa.common.domain.CommonException;

import java.time.LocalDate;

public class ResidentException extends CommonException {

    public enum ErrorCode{
        REGISTERED_AT_IN_FUTURE,
        UNIT_HAS_RESIDENTS
    }

    public static ResidentException registredAtInFuture(LocalDate date){
        return new ResidentException(ErrorCode.REGISTERED_AT_IN_FUTURE.toString(),
                "Registered Date [%s] is in future".formatted(date.toString()));
    }

    public static ResidentException unitHasResidents(Long unitId){
        return new ResidentException(ErrorCode.UNIT_HAS_RESIDENTS.toString(),
                "Unit id=[%d] has resident(s)".formatted(unitId));
    }

    public ResidentException(String errorCode, String errorDetails) {
        super(errorCode, errorDetails);
    }
}
