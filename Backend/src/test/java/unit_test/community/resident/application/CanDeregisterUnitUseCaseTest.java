package unit_test.community.resident.application;

import anatolii.k.hoa.community.resident.internal.application.CanDeregisterUnitUseCase;
import anatolii.k.hoa.community.resident.internal.domain.ResidentException;
import anatolii.k.hoa.community.resident.internal.domain.ResidentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CanDeregisterUnitUseCaseTest {

    @Mock
    private ResidentRepository residentRepository;

    @InjectMocks
    private CanDeregisterUnitUseCase canDeregisterUnitUseCase;

    @Test
    void whenUnitDoesNotHasResidents_thenOk(){
        Assertions.assertDoesNotThrow( ()-> canDeregisterUnitUseCase.check(1L) );
    }

    @Test
    void whenUnitHasResidents_thenThrowsException(){

        Long unitId = 3L;

        Mockito.when(residentRepository.hasResidentsInUnit(unitId)).thenReturn(true);

        ResidentException exception = catchThrowableOfType( ResidentException.class,
                () -> canDeregisterUnitUseCase.check(unitId));

        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(ResidentException.ErrorCode.UNIT_HAS_RESIDENTS.toString());
    }

}
