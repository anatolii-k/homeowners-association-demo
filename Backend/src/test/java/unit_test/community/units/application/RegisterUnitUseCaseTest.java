package unit_test.community.units.application;

import static org.assertj.core.api.Assertions.*;

import anatolii.k.hoa.community.unit.internal.application.RegisterUnitUseCase;
import anatolii.k.hoa.community.unit.internal.domain.Unit;
import anatolii.k.hoa.community.unit.internal.application.UnitException;
import anatolii.k.hoa.community.unit.internal.application.UnitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RegisterUnitUseCaseTest {
    @Mock
    UnitRepository unitRepository;

    @InjectMocks
    RegisterUnitUseCase registerUnitUseCase;


    @Test
    void whenRegisterUnitWhichDoesNotExist_thenOk(){

        final Unit expectedUnit = new Unit(1L, "AOO1", 50);

        Mockito.when( unitRepository.doesUnitExist(expectedUnit.number()) ).thenReturn(false);
        Mockito.when( unitRepository.save(new Unit(null, expectedUnit.number(), expectedUnit.area())))
                .thenReturn( expectedUnit);

        var response = registerUnitUseCase.register(expectedUnit.number(), expectedUnit.area());

        assertThat(response).isNotNull();
        assertThat(response.ok()).isTrue();
        assertThat(response.data()).isEqualTo(expectedUnit);
    }
    @Test
    void whenRegisterUnitWhichAlreadyExists_thenException(){

        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when( unitRepository.doesUnitExist(existingUnit.number()) ).thenReturn(true);

        var response = registerUnitUseCase.register(existingUnit.number(), existingUnit.area());

        assertThat(response).isNotNull();
        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(UnitException.ErrorCode.ALREADY_EXISTS.toString());
    }
}
