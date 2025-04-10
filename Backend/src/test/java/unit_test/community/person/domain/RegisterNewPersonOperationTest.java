package unit_test.community.person.domain;

import anatolii.k.hoa.common.domain.Email;
import anatolii.k.hoa.common.domain.PhoneNumber;
import anatolii.k.hoa.common.domain.SSN;
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
public class RegisterNewPersonOperationTest {
    @Mock
    PersonRepository personRepository;
    @InjectMocks
    RegisterNewPersonOperation registerNewPersonOperation;

    @Test
    void whenCorrectData_thenOk(){

        Person person = new Person( null,
                "Fname",
                "Lname",
                PhoneNumber.fromString("+380633003030"),
                Email.fromString("person@gmail.com"),
                SSN.fromString("1234567890") );


        Mockito.when(personRepository.existsPersonWithSSN(person.getSsn().toString())).thenReturn(false);

        Person registeredPerson = registerNewPersonOperation.register(person.getFirstName(),
                person.getLastName(), person.getPhoneNumber().toString(),
                person.getEmail().toString(), person.getSsn().toString());

        Mockito.verify(personRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenCorrectDataAndNoEmail_thenOk(){
        Person person = new Person( null,
                "Fname",
                "Lname",
                PhoneNumber.fromString("+380633003030"),
                null,
                SSN.fromString("1234567890") );


        Mockito.when(personRepository.existsPersonWithSSN(person.getSsn().toString())).thenReturn(false);

        Person registeredPerson = registerNewPersonOperation.register(person.getFirstName(),
                person.getLastName(), person.getPhoneNumber().toString(),
                null, person.getSsn().toString());

        Mockito.verify(personRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenPersonWithSuchSSNAlreadyExists_thenException(){
        Person person = new Person( null,
                "Fname",
                "Lname",
                PhoneNumber.fromString("+380633003030"),
                Email.fromString("person@gmail.com"),
                SSN.fromString("1234567890"));

        Mockito.when(personRepository.existsPersonWithSSN(person.getSsn().toString())).thenReturn(true);

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerNewPersonOperation.register(person.getFirstName(),
                        person.getLastName(), person.getPhoneNumber().toString(),
                        person.getEmail().toString(), person.getSsn().toString())
        );

        assertThat(exception.getErrorCode()).isEqualTo(PersonException.ErrorCode.SSN_ALREADY_EXISTS.toString());

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }


    @Test
    void whenNoSSN_thenException(){
        Person person = new Person( null,
                "Fname",
                "Lname",
                PhoneNumber.fromString("+380633003030"),
                Email.fromString("person@gmail.com"),
                null );

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerNewPersonOperation.register(person.getFirstName(),
                                    person.getLastName(), person.getPhoneNumber().toString(),
                                    person.getEmail().toString(), null)
        );

        assertThat(exception.getErrorCode()).isEqualTo(
                PersonAttributes.SSN.toString() + "_" + PersonException.ErrorCode.REQUIRED);

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenEmptyFirstName_thenException(){
        Person person = new Person( null,
                "Fname",
                "Lname",
                PhoneNumber.fromString("+380633003030"),
                Email.fromString("person@gmail.com"),
                SSN.fromString("1234567890") );

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerNewPersonOperation.register("",
                        person.getLastName(), person.getPhoneNumber().toString(),
                        person.getEmail().toString(), person.getSsn().toString())
        );

        assertThat(exception.getErrorCode()).isEqualTo(
                PersonAttributes.FIRST_NAME.toString() + "_" + PersonException.ErrorCode.REQUIRED);

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNoLastName_thenException(){
        Person person = new Person( null,
                "Fname",
                "Lname",
                PhoneNumber.fromString("+380633003030"),
                Email.fromString("person@gmail.com"),
                SSN.fromString("1234567890") );

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerNewPersonOperation.register(person.getFirstName(),
                        null, person.getPhoneNumber().toString(),
                        person.getEmail().toString(), person.getSsn().toString())
        );

        assertThat(exception.getErrorCode()).isEqualTo(
                PersonAttributes.LAST_NAME.toString() + "_" + PersonException.ErrorCode.REQUIRED);

        Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNoPhoneNumber_thenException(){
        Person person = new Person( null,
                "Fname",
                "Lname",
                PhoneNumber.fromString("+380633003030"),
                Email.fromString("person@gmail.com"),
                SSN.fromString("1234567890") );

        PersonException exception = catchThrowableOfType(PersonException.class,
                () -> registerNewPersonOperation.register(person.getFirstName(),
                        person.getLastName(), null,
                        person.getEmail().toString(), person.getSsn().toString())
        );

        assertThat(exception.getErrorCode()).isEqualTo(
                PersonAttributes.PHONE.toString() + "_" + PersonException.ErrorCode.REQUIRED);

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
