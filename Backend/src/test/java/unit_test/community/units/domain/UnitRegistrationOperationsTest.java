package unit_test.community.units.domain;

import anatolii.k.hoa.community.unit.domain.*;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UnitRegistrationOperationsTest {
    @Mock
    UnitRepository unitRepository;
    @Mock
    ResidentService residentService;

    @InjectMocks
    UnitRegistrationOperations unitRegistrationOperations;

    @Test
    void whenRegisterUnitWhichDoesNotExist_thenOk(){

        final Unit expectedUnit = new Unit(1L, "AOO1", 50);

        Mockito.when( unitRepository.doesUnitExist(expectedUnit.number()) ).thenReturn(false);
        Mockito.when( unitRepository.save(new Unit(null, expectedUnit.number(), expectedUnit.area())))
                .thenReturn( expectedUnit);

        Unit retUnit = unitRegistrationOperations.register(expectedUnit.number(), expectedUnit.area());

        assertThat(retUnit).isEqualTo(expectedUnit);
    }
    @Test
    void whenRegisterUnitWhichAlreadyExists_thenException(){

        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when( unitRepository.doesUnitExist(existingUnit.number()) ).thenReturn(true);

        UnitException exception = catchThrowableOfType( UnitException.class,
                ()-> unitRegistrationOperations.register(existingUnit.number(), existingUnit.area()) );

        assertThat(exception.getErrorCode()).isEqualTo(UnitException.ErrorCode.ALREADY_EXISTS.toString());
    }

    @Test
    void whenUnregisterExistingUnitWithoutResidents_thenOk(){
        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when(unitRepository.doesUnitExist(existingUnit.id())).thenReturn(true);
        Mockito.when(residentService.hasResidentsInUnit(existingUnit.id())).thenReturn(false);

        unitRegistrationOperations.unregister(existingUnit.id());

        Mockito.verify(unitRepository, Mockito.times(1)).delete(existingUnit.id());
    }

    @Test
    void whenUnregisterNotExistingUnit_thenException(){
        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when(unitRepository.doesUnitExist(existingUnit.id())).thenReturn(false);

        UnitException exception = catchThrowableOfType(UnitException.class,
                ()-> unitRegistrationOperations.unregister(existingUnit.id()));

        assertThat(exception.getErrorCode()).isEqualTo(UnitException.ErrorCode.NOT_EXISTS.toString());

        Mockito.verify(unitRepository, Mockito.times(0)).delete(existingUnit.id());
    }

    @Test
    void whenUnregisterExistingUnitWithAssignedResident_thenException(){
        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when(unitRepository.doesUnitExist(existingUnit.id())).thenReturn(true);
        Mockito.when(residentService.hasResidentsInUnit(existingUnit.id())).thenReturn(true);

        UnitException exception = catchThrowableOfType(UnitException.class,
                ()-> unitRegistrationOperations.unregister(existingUnit.id()));

        assertThat(exception.getErrorCode()).isEqualTo(UnitException.ErrorCode.HAS_RESIDENTS.toString());

        Mockito.verify(unitRepository, Mockito.times(0)).delete(existingUnit.id());
    }
}
