package anatolii.k.hoa.community.resident.domain;

import anatolii.k.hoa.common.domain.CommonException;

import java.time.LocalDate;

public class ResidentException extends CommonException {

    public enum ErrorCode{
        REGISTERED_AT_IN_FUTURE
    }

    public static ResidentException registredAtInFuture(LocalDate date){
        return new ResidentException(ErrorCode.REGISTERED_AT_IN_FUTURE.toString(),
                "Registered Date [%s] is in future".formatted(date.toString()));
    }

    public ResidentException(String errorCode, String errorDetails) {
        super(errorCode, errorDetails);
    }
}
