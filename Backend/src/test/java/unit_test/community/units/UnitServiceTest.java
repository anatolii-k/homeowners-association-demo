package unit_test.community.units;

import anatolii.k.hoa.community.unit.UnitService;
import anatolii.k.hoa.community.unit.internal.domain.UnitException;
import anatolii.k.hoa.community.unit.internal.domain.UnitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UnitServiceTest {

    @Mock
    UnitRepository unitRepository;
    @InjectMocks
    UnitService unitService;

    @Test
    void whenUnitExists_thenCheckUnitExistsIsOK(){

        Long unitId = 2L;
        Mockito.when(unitRepository.doesUnitExist(unitId)).thenReturn(true);

        Assertions.assertDoesNotThrow( () -> unitService.checkUnitExists(unitId) );
    }

    @Test
    void whenUnitDoesNotExist_thenCheckUnitExistsThrowsException(){

        Long unitId = 5L;
        UnitException exception = catchThrowableOfType(UnitException.class,
                () -> unitService.checkUnitExists(unitId));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(UnitException.ErrorCode.NOT_EXISTS.toString());
    }

    @Test
    void whenUnitIdIsNull_thenCheckUnitExistsThrowsException(){

        UnitException exception = catchThrowableOfType(UnitException.class,
                () -> unitService.checkUnitExists(null));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(UnitException.ErrorCode.NOT_EXISTS.toString());
    }

}
