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
public class DeregisterUnitOperationTest {

    @Mock
    UnitRepository unitRepository;
    @Mock
    ResidentService residentService;

    @InjectMocks
    DeregisterUnitOperation deregisterUnitOperation;


    @Test
    void whenDeregisterExistingUnitWithoutResidents_thenOk(){
        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when(unitRepository.doesUnitExist(existingUnit.id())).thenReturn(true);
        Mockito.when(residentService.hasResidentsInUnit(existingUnit.id())).thenReturn(false);

        deregisterUnitOperation.deregister(existingUnit.id());

        Mockito.verify(unitRepository, Mockito.times(1)).delete(existingUnit.id());
    }

    @Test
    void whenDeregisterNotExistingUnit_thenException(){
        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when(unitRepository.doesUnitExist(existingUnit.id())).thenReturn(false);

        UnitException exception = catchThrowableOfType(UnitException.class,
                ()-> deregisterUnitOperation.deregister(existingUnit.id()));

        assertThat(exception.getErrorCode()).isEqualTo(UnitException.ErrorCode.NOT_EXISTS.toString());

        Mockito.verify(unitRepository, Mockito.times(0)).delete(existingUnit.id());
    }

    @Test
    void whenDeregisterExistingUnitWithAssignedResident_thenException(){
        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when(unitRepository.doesUnitExist(existingUnit.id())).thenReturn(true);
        Mockito.when(residentService.hasResidentsInUnit(existingUnit.id())).thenReturn(true);

        UnitException exception = catchThrowableOfType(UnitException.class,
                ()-> deregisterUnitOperation.deregister(existingUnit.id()));

        assertThat(exception.getErrorCode()).isEqualTo(UnitException.ErrorCode.HAS_RESIDENTS.toString());

        Mockito.verify(unitRepository, Mockito.times(0)).delete(existingUnit.id());
    }

}
