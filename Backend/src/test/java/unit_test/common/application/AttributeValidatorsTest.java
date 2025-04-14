package unit_test.common.application;

import anatolii.k.hoa.common.application.AttributeValidators;
import anatolii.k.hoa.common.domain.CommonException;
import static org.assertj.core.api.Assertions.*;

import anatolii.k.hoa.common.domain.MoneyAmount;
import org.junit.jupiter.api.Test;

public class AttributeValidatorsTest {

    @Test
    void givenNotNullValidator_whenNotNullValue_thenOk(){
        AttributeValidators.notNull().validate("attr1", MoneyAmount.zero());
    }

    @Test
    void givenNotNullValidator_whenNullValue_thenException(){
        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> AttributeValidators.notNull().validate("attr1", null));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
    }

    @Test
    void givenNotEmptyValidator_whenNotEmptyString_thenOk(){
        AttributeValidators.notEmpty().validate("attr1", "value");
    }

    @Test
    void givenNotEmptyValidator_whenNullValue_thenException(){
        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> AttributeValidators.notEmpty().validate("attr1", null));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
    }

    @Test
    void givenNotEmptyValidator_whenEmptyValue_thenException(){
        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> AttributeValidators.notEmpty().validate("attr1", ""));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
    }

    @Test
    void givenNotNegativeValidator_whenPositiveValue_thenOk(){
        AttributeValidators.notNegative().validate("attr1", 100);
    }

    @Test
    void givenNotNegativeValidator_whenZeroValue_thenOk(){
        AttributeValidators.notNegative().validate("attr1", 0L);
    }

    @Test
    void givenNotNegativeValidator_whenNullValue_thenOk(){
        AttributeValidators.notNegative().validate("attr1", null);
    }


    @Test
    void givenNotNegativeValidator_whenNegativeValue_thenException(){
        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> AttributeValidators.notNegative().validate("attr1", -1L));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.NEGATIVE_VALUE.toString());
    }
}
