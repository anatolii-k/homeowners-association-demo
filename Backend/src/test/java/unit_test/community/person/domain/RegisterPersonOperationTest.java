package unit_test.community.person.domain;

import anatolii.k.hoa.community.person.domain.*;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RegisterPersonOperationTest {
    @Mock
    PersonRepository personRepository;
    @InjectMocks
    RegisterPersonOperation registerPersonOperation;

    @Test
    void whenCorrectData_thenOk(){
        RegisterPersonRequest request = new RegisterPersonRequest(
                "Fname",
                "Lname",
                "+380633003030",
                "person@gmail.com",
                "1234567890" );

        Mockito.when(personRepository.existsPersonWithSSN(request.getSsn())).thenReturn(false);

        registerPersonOperation.register(request);

        Mockito.verify(personRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenCorrectDataAndNoEmail_thenOk(){
        RegisterPersonRequest request = new RegisterPersonRequest(
                "Fname",
                "Lname",
                "+380633003030",
                null,
                "1234567890");


        Mockito.when(personRepository.existsPersonWithSSN(request.getSsn())).thenReturn(false);

        registerPersonOperation.register(request);

        Mockito.verify(personRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenPersonWithSuchSSNAlreadyExists_thenException(){
        RegisterPersonRequest request = new RegisterPersonRequest(
                "Fname",
                "Lname",
                "+380633003030",
                "person@gmail.com",
                "1234567890");

        Mockito.when(personRepository.existsPersonWithSSN(request.getSsn())).thenReturn(true);

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerPersonOperation.register( request )
        );

        assertThat(exception.getErrorCode()).isEqualTo(PersonException.ErrorCode.SSN_ALREADY_EXISTS.toString());

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNoSSN_thenException(){
        RegisterPersonRequest request = new RegisterPersonRequest(
                "Fname",
                "Lname",
                "+380633003030",
                "person@gmail.com",
                null );

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerPersonOperation.register(request)
        );

        assertThat(exception.getErrorCode()).isEqualTo(
                PersonAttributes.SSN + "_" + PersonException.ErrorCode.REQUIRED);

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenEmptyFirstName_thenException(){
        RegisterPersonRequest request = new RegisterPersonRequest(
                "",
                "Lname",
                "+380633003030",
                "person@gmail.com",
                "1234567890");

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerPersonOperation.register(request)
        );

        assertThat(exception.getErrorCode()).isEqualTo(
                PersonAttributes.FIRST_NAME + "_" + PersonException.ErrorCode.REQUIRED);

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNoLastName_thenException(){
        RegisterPersonRequest request = new RegisterPersonRequest(
                "Fname",
                "",
                "+380633003030",
                "person@gmail.com",
                "1234567890");

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerPersonOperation.register(request)
        );

        assertThat(exception.getErrorCode()).isEqualTo(
                PersonAttributes.LAST_NAME + "_" + PersonException.ErrorCode.REQUIRED);

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNoPhoneNumber_thenException(){
        RegisterPersonRequest request = new RegisterPersonRequest(
                "Fname",
                "Lname",
                "",
                "person@gmail.com",
                "1234567890");

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerPersonOperation.register(request)
        );

        assertThat(exception.getErrorCode()).isEqualTo(
                PersonAttributes.PHONE + "_" + PersonException.ErrorCode.REQUIRED);

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Disabled
    @Test
    void whenInvalidSSN_thenException(){

    }

    @Disabled
    @Test
    void whenInvalidPhoneNumber_thenException(){

    }

    @Disabled
    @Test
    void whenInvalidEmail_thenException(){

    }

}
