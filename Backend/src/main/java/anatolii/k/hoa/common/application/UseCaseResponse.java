package anatolii.k.hoa.common.application;

public record UseCaseResponse<T>( boolean ok, String error, T data) {

    public static class Builder<T>{

        public Builder<T> ok( boolean isOk ){
            ok = isOk;
            return this;
        }

        public Builder<T> error( String error ){
            this.error = error;
            return this;
        }

        public Builder<T> data( T data ){
            this.data = data;
            return this;
        }

        public UseCaseResponse<T> build(){
            return new UseCaseResponse<T>(ok, error, data);
        }

        private boolean ok = true;
        private String error;
        T data;
    }

    public static <T> Builder<T> builder(){
        return new Builder<>();
    }
}
