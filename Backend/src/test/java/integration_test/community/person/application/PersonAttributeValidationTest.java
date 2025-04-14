package integration_test.community.person.application;

import anatolii.k.hoa.HoaApplication;
import anatolii.k.hoa.common.application.AttributeValidators;
import anatolii.k.hoa.common.domain.CommonException;
import anatolii.k.hoa.community.person.internal.application.PersonAttributesValidation;
import anatolii.k.hoa.community.person.internal.application.PersonDTO;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = HoaApplication.class)
@TestPropertySource(
        properties = { "hoa.person.attributes.required=FIRST_NAME,LAST_NAME,SSN" }
)
public class PersonAttributeValidationTest {
    @Autowired
    PersonAttributesValidation validation;

    @Test
    void whenAllRequiredAttributesAreNotEmpty_thenOk(){
        PersonDTO personData = new PersonDTO(null, "Fname", "Lname", "", null, "111111111111111");

        validation.validate(personData);
    }

    @Test
    void whenFirstNameIsEmpty_thenThrowException(){
        PersonDTO personData = new PersonDTO(null, "", "Lname", "", "", "111111111111111");

        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> validation.validate(personData));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
    }

    @Test
    void whenLastNameIsNull_thenThrowException(){
        PersonDTO personData = new PersonDTO(null, "Fname", null, "", "", "111111111111111");

        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> validation.validate(personData));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
    }

    @Test
    void whenSSNIsEmpty_thenThrowException(){
        PersonDTO personData = new PersonDTO(null, "Fname", "Lname", "", "", "");

        CommonException exception = catchThrowableOfType(CommonException.class,
                () -> validation.validate(personData));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
    }


}
