package unit_test.common.application;

import anatolii.k.hoa.common.application.AttributeValidators;
import anatolii.k.hoa.common.application.AttributesValidation;
import anatolii.k.hoa.common.domain.CommonException;
import anatolii.k.hoa.common.domain.MoneyAmount;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AttributesValidationTest {

    private static final AttributesValidation validation = new AttributesValidation();
    private enum AttrNames{
        NO_VALIDATION,
        NOT_NULL,
        NOT_EMPTY,
        NOT_NEGATIVE_ONLY,
        NOT_NEGATIVE_AND_NOT_NULL
    }

    @BeforeAll
    static void setupAttributesValidation(){
        validation.registerValidator( AttrNames.NOT_NULL.toString(), AttributeValidators.notNull());
        validation.registerValidator( AttrNames.NOT_EMPTY.toString(), AttributeValidators.notEmpty());
        validation.registerValidator( AttrNames.NOT_NEGATIVE_ONLY.toString(), AttributeValidators.notNegative());

        validation.registerValidator( AttrNames.NOT_NEGATIVE_AND_NOT_NULL.toString(), AttributeValidators.notNull());
        validation.registerValidator( AttrNames.NOT_NEGATIVE_AND_NOT_NULL.toString(), AttributeValidators.notNegative());
    }

    @Test
    void givenAttributeWithoutValidators_whenNullValue_thenOk(){
         validation.validate( AttrNames.NO_VALIDATION.toString(), null);
    }

    @Test
    void givenAttributeWithoutValidators_whenEmptyStringValue_thenOk(){
        validation.validate( AttrNames.NO_VALIDATION.toString(), "");
    }

    @Test
    void givenAttributeWithoutValidators_whenNegativeLong_thenOk(){
        validation.validate( AttrNames.NO_VALIDATION.toString(), -10L);
    }

    @Test
    void givenAttributeWithNotNullValidator_whenNotNullValue_thenOk(){
        validation.validate( AttrNames.NO_VALIDATION.toString(), MoneyAmount.zero());
    }

    @Test
    void givenAttributeWithNotNullValidator_whenNullValue_thenException(){
        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> validation.validate( AttrNames.NOT_NULL.toString(), null));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
    }

    @Test
    void givenAttributeWithNotEmptyValidator_whenNotEmptyValue_thenOk(){
        validation.validate( AttrNames.NOT_EMPTY.toString(), "A");
    }

    @Test
    void givenAttributeWithNotEmptyValidator_whenEmptyValue_thenException(){
        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> validation.validate( AttrNames.NOT_EMPTY.toString(), ""));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
    }

    @Test
    void givenAttributeWithNotEmptyValidator_whenNullValue_thenException(){
        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> validation.validate( AttrNames.NOT_EMPTY.toString(), null));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
    }

    @Test
    void givenAttributeWithNotNegativeValidatorOnly_whenPositiveValue_thenOk(){
        validation.validate( AttrNames.NOT_NEGATIVE_ONLY.toString(), 10L);
    }

    @Test
    void givenAttributeWithNotNegativeValidatorOnly_whenNullValue_thenOk(){
        validation.validate( AttrNames.NOT_NEGATIVE_ONLY.toString(), null);
    }

    @Test
    void givenAttributeWithNotNegativeValidatorOnly_whenNegativeValue_thenException(){
        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> validation.validate( AttrNames.NOT_NEGATIVE_ONLY.toString(), -100L));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.NEGATIVE_VALUE.toString());
    }

    @Test
    void givenAttributeWithNotNegativeAndNotNullValidators_whenPositiveValue_thenOk(){
        validation.validate( AttrNames.NOT_NEGATIVE_AND_NOT_NULL.toString(), 1L);
    }

    @Test
    void givenAttributeWithNotNegativeAndNotNullValidators_whenNegativeValue_thenException(){
        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> validation.validate( AttrNames.NOT_NEGATIVE_AND_NOT_NULL.toString(), -1L));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.NEGATIVE_VALUE.toString());
    }

    @Test
    void givenAttributeWithNotNegativeAndNotNullValidators_whenNullValue_thenException(){
        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> validation.validate( AttrNames.NOT_NEGATIVE_AND_NOT_NULL.toString(), null));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
    }

}
