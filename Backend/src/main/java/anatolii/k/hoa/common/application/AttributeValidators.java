package anatolii.k.hoa.common.application;

import anatolii.k.hoa.common.domain.CommonException;

public class AttributeValidators {

    public enum ErrorCode{
        EMPTY_VALUE,
        NEGATIVE_VALUE
    }

    public static AttributeValidator<Object> notNull() {
        return (name, value) -> {
            if (value == null) {
                throw new CommonException(ErrorCode.EMPTY_VALUE.toString(),
                        name + " is empty");
            }
        };
    }

    public static AttributeValidator<String> notEmpty(){
        return (name, value) -> {
           if(value == null || value.isBlank()){
              throw new CommonException(ErrorCode.EMPTY_VALUE.toString(),
                      name + " is empty");
           }
        };
    }

    public static AttributeValidator<Number> notNegative(){
        return (name, value) -> {
            if(value != null && value.doubleValue() < 0){
                throw new CommonException(ErrorCode.NEGATIVE_VALUE.toString(),
                        name + " has negative value");
            }
        };
    }
}
