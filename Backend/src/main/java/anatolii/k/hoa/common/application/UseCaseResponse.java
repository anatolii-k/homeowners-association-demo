package anatolii.k.hoa.common.application;

public record UseCaseResponse<T>( boolean ok, String errorCode, String errorDetails, T data) {

    public static class Builder<T>{

        public Builder<T> ok( boolean isOk ){
            ok = isOk;
            return this;
        }

        public Builder<T> errorCode( String errorCode ){
            this.errorCode = errorCode;
            return this;
        }

        public Builder<T> errorDetails( String errorDetails ){
            this.errorDetails = errorDetails;
            return this;
        }

        public Builder<T> data( T data ){
            this.data = data;
            return this;
        }

        public UseCaseResponse<T> build(){
            return new UseCaseResponse<>(ok, errorCode, errorDetails, data);
        }

        private boolean ok = true;
        private String errorCode;
        private String errorDetails;
        T data;
    }

    public static <T> Builder<T> builder(){
        return new Builder<>();
    }
}
