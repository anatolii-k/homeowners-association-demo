package anatolii.k.hoa.common.domain;

public class CommonException extends RuntimeException {
    private final String errorCode;

    public CommonException(String errorCode, String errorDetails) {
        super(errorDetails);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
