package unit_test.community.units.application;

import static org.assertj.core.api.Assertions.*;

import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.common.domain.CommonException;
import anatolii.k.hoa.community.unit.BeforeDeregisterUnitEvent;
import anatolii.k.hoa.community.unit.internal.application.DeregisterUnitUseCase;
import anatolii.k.hoa.community.unit.internal.application.UnitException;
import anatolii.k.hoa.community.unit.internal.application.UnitRepository;
import anatolii.k.hoa.community.unit.internal.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class DeregisterUnitOperationTest {

    @Mock
    private UnitRepository unitRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DeregisterUnitUseCase deregisterUnitOperation;


    @Test
    void whenDeregisterExistingUnitWithoutResidents_thenOk(){
        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when(unitRepository.doesUnitExist(existingUnit.id())).thenReturn(true);

        UseCaseResponse<Void> response = deregisterUnitOperation.deregister(existingUnit.id());
        assertThat(response).isNotNull();
        assertThat(response.ok()).isTrue();

        Mockito.verify(unitRepository, Mockito.times(1)).delete(existingUnit.id());
    }

    @Test
    void whenDeregisterNotExistingUnit_thenException(){
        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when(unitRepository.doesUnitExist(existingUnit.id())).thenReturn(false);

        UseCaseResponse<Void> response = deregisterUnitOperation.deregister(existingUnit.id());
        assertThat(response).isNotNull();
        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(UnitException.ErrorCode.NOT_EXISTS.toString());

        Mockito.verify(unitRepository, Mockito.times(0)).delete(existingUnit.id());
    }

    @Test
    void whenDeregisterUnitIsPreventedByEvent_thenException(){
        final Unit existingUnit = new Unit(1L, "AOO1", 50);
        Mockito.when(unitRepository.doesUnitExist(existingUnit.id())).thenReturn(true);
        Mockito.doThrow(CommonException.class)
                .when(eventPublisher).publishEvent(Mockito.any(BeforeDeregisterUnitEvent.class));

        UseCaseResponse<Void> response = deregisterUnitOperation.deregister(existingUnit.id());
        assertThat(response).isNotNull();
        assertThat(response.ok()).isFalse();

        Mockito.verify(unitRepository, Mockito.times(0)).delete(existingUnit.id());
    }

}
