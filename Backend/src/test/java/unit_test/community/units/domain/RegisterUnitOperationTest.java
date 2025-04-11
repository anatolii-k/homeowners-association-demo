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
public class RegisterUnitOperationTest {
    @Mock
    UnitRepository unitRepository;

    @InjectMocks
    RegisterUnitOperation registerUnitOperation;


    @Test
    void whenRegisterUnitWhichDoesNotExist_thenOk(){

        final Unit expectedUnit = new Unit(1L, "AOO1", 50);

        Mockito.when( unitRepository.doesUnitExist(expectedUnit.number()) ).thenReturn(false);
        Mockito.when( unitRepository.save(new Unit(null, expectedUnit.number(), expectedUnit.area())))
                .thenReturn( expectedUnit);

        Unit retUnit = registerUnitOperation.register(expectedUnit.number(), expectedUnit.area());

        assertThat(retUnit).isEqualTo(expectedUnit);
    }
    @Test
    void whenRegisterUnitWhichAlreadyExists_thenException(){

        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when( unitRepository.doesUnitExist(existingUnit.number()) ).thenReturn(true);

        UnitException exception = catchThrowableOfType( UnitException.class,
                ()-> registerUnitOperation.register(existingUnit.number(), existingUnit.area()) );

        assertThat(exception.getErrorCode()).isEqualTo(UnitException.ErrorCode.ALREADY_EXISTS.toString());
    }
}
