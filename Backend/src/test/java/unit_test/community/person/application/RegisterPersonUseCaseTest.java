package unit_test.community.person.application;

import static org.assertj.core.api.Assertions.*;

import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.common.domain.PhoneNumber;
import anatolii.k.hoa.community.person.internal.application.*;
import anatolii.k.hoa.community.person.internal.domain.Person;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RegisterPersonUseCaseTest {
    @Mock
    PersonRepository personRepository;
    @InjectMocks
    RegisterPersonUseCase registerPersonUseCase;

    @Test
    void whenCorrectData_thenOk(){
        PersonDTO request = new PersonDTO( null,
                "Fname",
                "Lname",
                "+380633003030",
                "person@gmail.com",
                "1234567890" );

        Mockito.when(personRepository.existsPersonWithSSN(request.getSsn())).thenReturn(false);

        UseCaseResponse<Person> response = registerPersonUseCase.register(request);
        assertThat(response.ok()).isTrue();

        Mockito.verify(personRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenCorrectDataAndNoEmail_thenOk(){
        PersonDTO request = new PersonDTO( null,
                "Fname",
                "Lname",
                "+380633003030",
                null,
                "1234567890");


        Mockito.when(personRepository.existsPersonWithSSN(request.getSsn())).thenReturn(false);

        UseCaseResponse<Person> response = registerPersonUseCase.register(request);
        assertThat(response.ok()).isTrue();

        Mockito.verify(personRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenPersonWithSuchSSNAlreadyExists_thenException(){
        PersonDTO request = new PersonDTO( null,
                "Fname",
                "Lname",
                "+380633003030",
                "person@gmail.com",
                "1234567890");

        Mockito.when(personRepository.existsPersonWithSSN(request.getSsn())).thenReturn(true);

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerPersonUseCase.register( request )
        );

        UseCaseResponse<Person> response = registerPersonUseCase.register(request);
        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(PersonException.ErrorCode.SSN_ALREADY_EXISTS.toString());

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNoSSN_thenException(){
        PersonDTO request = new PersonDTO( null,
                "Fname",
                "Lname",
                "+380633003030",
                "person@gmail.com",
                null );

        UseCaseResponse<Person> response = registerPersonUseCase.register(request);
        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(PersonAttributes.SSN + "_" + PersonException.ErrorCode.REQUIRED);

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenEmptyFirstName_thenException(){
        PersonDTO request = new PersonDTO( null,
                "",
                "Lname",
                "+380633003030",
                "person@gmail.com",
                "1234567890");

        UseCaseResponse<Person> response = registerPersonUseCase.register(request);
        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(PersonAttributes.FIRST_NAME + "_" + PersonException.ErrorCode.REQUIRED);

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNoLastName_thenException(){
        PersonDTO request = new PersonDTO( null,
                "Fname",
                "",
                "+380633003030",
                "person@gmail.com",
                "1234567890");

        UseCaseResponse<Person> response = registerPersonUseCase.register(request);
        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(PersonAttributes.LAST_NAME + "_" + PersonException.ErrorCode.REQUIRED);

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNoPhoneNumber_thenException(){
        PersonDTO request = new PersonDTO( null,
                "Fname",
                "Lname",
                "",
                "person@gmail.com",
                "1234567890");

        UseCaseResponse<Person> response = registerPersonUseCase.register(request);
        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(PersonAttributes.PHONE + "_" + PersonException.ErrorCode.REQUIRED);
    }

    @Disabled
    @Test
    void whenInvalidSSN_thenException(){

    }

    @Test
    void whenInvalidPhoneNumber_thenException(){
        PersonDTO request = new PersonDTO( null,
                "Fname",
                "Lname",
                "xxx",
                "person@gmail.com",
                "1234567890");

        UseCaseResponse<Person> response = registerPersonUseCase.register(request);
        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(PhoneNumber.ErrorCode.INVALID_PHONE_NUMBER.toString());
    }

    @Disabled
    @Test
    void whenInvalidEmail_thenException(){

    }

}
