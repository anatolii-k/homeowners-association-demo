package unit_test.common.domain;

import anatolii.k.hoa.common.domain.CommonException;
import anatolii.k.hoa.common.domain.PhoneNumber;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PhoneNumberTest {

    @Test
    void whenValidPhoneNumberFormatted_thenOk(){
        PhoneNumber number = PhoneNumber.fromString("+ 38 (067)  900 1010");

        assertThat(number).isNotNull();
        assertThat(number.getFormatedString()).isEqualTo("+38(067)900-10-10");
    }

    @Test
    void whenValidPhoneNumberNotFormatted_thenOk(){
        PhoneNumber number = PhoneNumber.fromString("+380679001010");

        assertThat(number).isNotNull();
        assertThat(number.getFormatedString()).isEqualTo("+38(067)900-10-10");
    }

    @Test
    void whenValidPhoneNumberWithoutLeadingPlusSigh_thenOk(){
        PhoneNumber number = PhoneNumber.fromString("380679001010");

        assertThat(number).isNotNull();
        assertThat(number.getFormatedString()).isEqualTo("+38(067)900-10-10");
    }

    @Test
    void whenValidPhoneNumberWithoutCountryCodeFormatted_thenOk(){
        PhoneNumber.setDefaultCountryCode("38");
        PhoneNumber number = PhoneNumber.fromString(" (067) 900-1010");

        assertThat(number).isNotNull();
        assertThat(number.getFormatedString()).isEqualTo("+38(067)900-10-10");
    }

    @Test
    void whenEmptyString_thenReturnNull(){
        PhoneNumber number = PhoneNumber.fromString("");
        assertThat(number).isNull();
    }

    @Test
    void whenNullString_thenReturnNull(){
        PhoneNumber number = PhoneNumber.fromString(null);
        assertThat(number).isNull();
    }


    @Test
    void whenWrongNumberOfDigits_thenException(){
        {
            CommonException exception = catchThrowableOfType(CommonException.class,
                    () -> PhoneNumber.fromString("+ 38 (067)  900 1010 9"));

            assertThat(exception).isNotNull();
            assertThat(exception.getErrorCode()).isEqualTo(PhoneNumber.ErrorCode.INVALID_PHONE_NUMBER.toString());
        }
        {
            CommonException exception = catchThrowableOfType(CommonException.class,
                    () -> PhoneNumber.fromString("(067)900 1010 9"));

            assertThat(exception).isNotNull();
            assertThat(exception.getErrorCode()).isEqualTo(PhoneNumber.ErrorCode.INVALID_PHONE_NUMBER.toString());
        }
        {
            CommonException exception = catchThrowableOfType(CommonException.class,
                    () -> PhoneNumber.fromString("(067)900 10"));

            assertThat(exception).isNotNull();
            assertThat(exception.getErrorCode()).isEqualTo(PhoneNumber.ErrorCode.INVALID_PHONE_NUMBER.toString());
        }
    }

    @Test
    void whenContainsUnexpectedSymbols_thenException(){
        {
            CommonException exception = catchThrowableOfType(CommonException.class,
                    () -> PhoneNumber.fromString("+38+067+900+10+10"));

            assertThat(exception).isNotNull();
            assertThat(exception.getErrorCode()).isEqualTo(PhoneNumber.ErrorCode.INVALID_PHONE_NUMBER.toString());
        }
        {
            CommonException exception = catchThrowableOfType(CommonException.class,
                    () -> PhoneNumber.fromString("+38 067 900 10 ??"));

            assertThat(exception).isNotNull();
            assertThat(exception.getErrorCode()).isEqualTo(PhoneNumber.ErrorCode.INVALID_PHONE_NUMBER.toString());
        }
        {
            CommonException exception = catchThrowableOfType(CommonException.class,
                    () -> PhoneNumber.fromString("+38 067 900 10 AA"));

            assertThat(exception).isNotNull();
            assertThat(exception.getErrorCode()).isEqualTo(PhoneNumber.ErrorCode.INVALID_PHONE_NUMBER.toString());
        }
    }




}
