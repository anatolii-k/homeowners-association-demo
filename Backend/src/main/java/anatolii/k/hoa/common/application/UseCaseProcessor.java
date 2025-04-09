package anatolii.k.hoa.common.application;

import anatolii.k.hoa.common.domain.CommonException;

import java.util.function.Supplier;

public class UseCaseProcessor {

    private static final String GENERAL_ERROR = "GENERAL_ERROR";

    public static <T> UseCaseResponse<T> process(Supplier<T> useCaseFunc){
        var responseBuilder = UseCaseResponse.<T>builder();
        try{
            T data = useCaseFunc.get();
            responseBuilder.ok(true)
                    .data( data );
        }
        catch(CommonException ex){
            responseBuilder.ok(false)
                    .errorCode(ex.getErrorCode())
                    .errorDetails(ex.getMessage());
        }
        catch (Throwable ex){
            responseBuilder.ok(false)
                    .errorCode(GENERAL_ERROR)
                    .errorDetails(ex.getMessage());
        }
        return responseBuilder.build();
    }

    public static UseCaseResponse<Void> process( Runnable useCaseFunc ){
        var responseBuilder = UseCaseResponse.<Void>builder();
        try{
            useCaseFunc.run();
            responseBuilder.ok(true);
        }
        catch(CommonException ex){
            responseBuilder.ok(false)
                    .errorCode(ex.getErrorCode())
                    .errorDetails(ex.getMessage());
        }
        catch (Throwable ex){
            responseBuilder.ok(false)
                    .errorCode(GENERAL_ERROR)
                    .errorDetails(ex.getMessage());
        }
        return responseBuilder.build();
    }
}
