package unit_test.community.resident.domain;

import anatolii.k.hoa.community.person.internal.domain.PersonException;
import anatolii.k.hoa.community.resident.internal.domain.*;
import anatolii.k.hoa.community.unit.internal.domain.UnitException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class RegisterResidentOperationTest {

    @Mock
    private UnitServiceClient unitServiceClient;
    @Mock
    private PersonServiceClient personServiceClient;
    @Mock
    private ResidentRepository residentRepository;

    @InjectMocks
    private RegisterResidentOperation registerResidentOperation;

    @Test
    void whenExistingUnitAndPersonAndValidDate_thenOk(){

        Long unitId = 1L;
        Long personId = 2L;
        LocalDate registeredAt = LocalDate.now().minusDays(2);

        Mockito.when(unitServiceClient.doesUnitExist(unitId)).thenReturn(true);
        Mockito.when(personServiceClient.doesPersonExist(personId)).thenReturn(true);

        registerResidentOperation.register(new ResidentRecord(null, personId, unitId, registeredAt ));

        Mockito.verify(residentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenExistingUnitAndPersonAndNullDate_thenOk(){

        Long unitId = 1L;
        Long personId = 2L;

        Mockito.when(unitServiceClient.doesUnitExist(unitId)).thenReturn(true);
        Mockito.when(personServiceClient.doesPersonExist(personId)).thenReturn(true);

        registerResidentOperation.register(new ResidentRecord(null, personId, unitId, null ));

        Mockito.verify(residentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenFutureRegisteredAtDate_thenException(){

        Long unitId = 1L;
        Long personId = 2L;
        LocalDate registeredAt = LocalDate.now().plusDays(1);

        Mockito.when(unitServiceClient.doesUnitExist(unitId)).thenReturn(true);
        Mockito.when(personServiceClient.doesPersonExist(personId)).thenReturn(true);

        ResidentException exception = catchThrowableOfType( ResidentException.class,
                ()-> registerResidentOperation.register(new ResidentRecord(null, personId, unitId, registeredAt )));

        assertThat(exception.getErrorCode()).isEqualTo(ResidentException.ErrorCode.REGISTERED_AT_IN_FUTURE.toString());

        Mockito.verify(residentRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNonExistingUnit_thenException(){

        Long unitId = 1L;
        Long personId = 2L;
        LocalDate registeredAt = LocalDate.now().minusDays(1);

        Mockito.when(unitServiceClient.doesUnitExist(unitId)).thenReturn(false);

        UnitException exception = catchThrowableOfType( UnitException.class,
                ()-> registerResidentOperation.register(new ResidentRecord(null, personId, unitId, registeredAt )));

        assertThat(exception.getErrorCode()).isEqualTo(UnitException.ErrorCode.NOT_EXISTS.toString());

        Mockito.verify(residentRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenNonExistingPerson_thenException(){

        Long unitId = 1L;
        Long personId = 2L;
        LocalDate registeredAt = LocalDate.now().minusDays(1);

        Mockito.when(unitServiceClient.doesUnitExist(unitId)).thenReturn(true);
        Mockito.when(personServiceClient.doesPersonExist(personId)).thenReturn(false);

        PersonException exception = catchThrowableOfType( PersonException.class,
                ()-> registerResidentOperation.register(new ResidentRecord(null, personId, unitId, registeredAt )));

        assertThat(exception.getErrorCode()).isEqualTo(PersonException.ErrorCode.NOT_EXISTS.toString());

        Mockito.verify(residentRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenPersonIdIsNull_thenException(){
        Long unitId = 1L;
        LocalDate registeredAt = LocalDate.now().minusDays(1);

        Mockito.when(unitServiceClient.doesUnitExist(unitId)).thenReturn(true);

        PersonException exception = catchThrowableOfType( PersonException.class,
                ()-> registerResidentOperation.register(new ResidentRecord(null, null, unitId, registeredAt )));

        assertThat(exception.getErrorCode()).isEqualTo(PersonException.ErrorCode.NOT_EXISTS.toString());

        Mockito.verify(residentRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void whenUnitIdIsNull_thenException(){
        Long personId = 2L;
        LocalDate registeredAt = LocalDate.now().minusDays(1);

        UnitException exception = catchThrowableOfType( UnitException.class,
                ()-> registerResidentOperation.register(new ResidentRecord(null, personId, null, registeredAt )));

        assertThat(exception.getErrorCode()).isEqualTo(UnitException.ErrorCode.NOT_EXISTS.toString());

        Mockito.verify(residentRepository, Mockito.times(0)).save(Mockito.any());
    }
}
