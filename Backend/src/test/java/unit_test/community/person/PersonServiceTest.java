package unit_test.community.person;

import anatolii.k.hoa.community.person.PersonService;
import anatolii.k.hoa.community.person.internal.application.PersonException;
import anatolii.k.hoa.community.person.internal.application.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    PersonService personService;

    @Test
    void whenPersonExists_thenCheckPersonExistsIsOk(){
        Long personId = 2L;
        Mockito.when(personRepository.existsPersonWithId(personId)).thenReturn(true);

        Assertions.assertDoesNotThrow( () -> personService.checkPersonExists(personId));
    }

    @Test
    void whenPersonDoesNotExists_thenCheckPersonExistsThrowsException(){

        Long personId = 6L;
        PersonException exception = catchThrowableOfType(PersonException.class,
                ()-> personService.checkPersonExists(personId));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(PersonException.ErrorCode.NOT_EXISTS.toString());
    }

    @Test
    void whenPersonIdIsNull_thenCheckPersonExistsThrowsException(){

        PersonException exception = catchThrowableOfType(PersonException.class,
                ()-> personService.checkPersonExists(null));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(PersonException.ErrorCode.NOT_EXISTS.toString());
    }

}
