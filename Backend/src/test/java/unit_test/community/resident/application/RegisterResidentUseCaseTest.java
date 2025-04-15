package unit_test.community.resident.application;

import anatolii.k.hoa.community.person.PersonService;
import anatolii.k.hoa.community.person.internal.application.PersonException;
import anatolii.k.hoa.community.resident.internal.application.RegisterResidentUseCase;
import anatolii.k.hoa.community.resident.internal.application.ResidentException;
import anatolii.k.hoa.community.resident.internal.application.ResidentRepository;
import anatolii.k.hoa.community.resident.internal.domain.*;
import anatolii.k.hoa.community.unit.UnitService;
import anatolii.k.hoa.community.unit.internal.application.UnitException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class RegisterResidentUseCaseTest {

    @Mock
    private UnitService unitService;
    @Mock
    private PersonService personService;
    @Mock
    private ResidentRepository residentRepository;

    @InjectMocks
    private RegisterResidentUseCase registerPersonUseCase;

    @Test
    void whenExistingUnitAndPersonAndValidDate_thenOk(){

        Long unitId = 1L;
        Long personId = 2L;
        LocalDate registeredAt = LocalDate.now().minusDays(2);

        var response = registerPersonUseCase.register(new ResidentRecord(null, personId, unitId, registeredAt ));
        assertThat(response.ok()).isTrue();

        Mockito.verify(residentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenExistingUnitAndPersonAndNullDate_thenOk(){

        Long unitId = 1L;
        Long personId = 2L;

        var response = registerPersonUseCase.register(new ResidentRecord(null, personId, unitId, null ));
        assertThat(response.ok()).isTrue();

        Mockito.verify(residentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenFutureRegisteredAtDate_thenException(){

        Long unitId = 1L;
        Long personId = 2L;
        LocalDate registeredAt = LocalDate.now().plusDays(10);

        var response = registerPersonUseCase.register(new ResidentRecord(null, personId, unitId, registeredAt ));

        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(ResidentException.ErrorCode.REGISTERED_AT_IN_FUTURE.toString());

        Mockito.verify(residentRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNonExistingUnit_thenException(){

        Long unitId = 1L;
        Long personId = 2L;
        LocalDate registeredAt = LocalDate.now().minusDays(1);

        Mockito.doThrow(UnitException.notExists(unitId))
                .when(unitService).checkUnitExists(unitId);

        var response = registerPersonUseCase.register(new ResidentRecord(null, personId, unitId, registeredAt ));

        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(UnitException.ErrorCode.NOT_EXISTS.toString());

        Mockito.verify(residentRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNonExistingPerson_thenException(){

        Long unitId = 1L;
        Long personId = 2L;
        LocalDate registeredAt = LocalDate.now().minusDays(1);

        Mockito.doThrow(PersonException.notExists(personId)).when(personService).checkPersonExists(personId);

        var response = registerPersonUseCase.register(new ResidentRecord(null, personId, unitId, registeredAt ));

        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(UnitException.ErrorCode.NOT_EXISTS.toString());

        Mockito.verify(residentRepository, Mockito.times(0)).save(Mockito.any());
    }
}
