package unit_test.community.units.domain;

import static org.assertj.core.api.Assertions.*;

import anatolii.k.hoa.community.unit.internal.domain.*;
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

    @InjectMocks
    DeregisterUnitOperation deregisterUnitOperation;


    @Test
    void whenDeregisterExistingUnitWithoutResidents_thenOk(){
        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when(unitRepository.doesUnitExist(existingUnit.id())).thenReturn(true);

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
}
