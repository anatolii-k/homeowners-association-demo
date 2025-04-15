package anatolii.k.hoa.community.resident.internal.events;

import anatolii.k.hoa.community.resident.internal.application.CanDeregisterUnitUseCase;
import anatolii.k.hoa.community.unit.BeforeDeregisterUnitEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BeforeDeregisterUnitHandler {

    @EventListener
    void onBeforeDeregisterUnit(BeforeDeregisterUnitEvent event){
        canDeregisterUnitUseCase.check(event.unitId());
    }

    public BeforeDeregisterUnitHandler(CanDeregisterUnitUseCase canDeregisterUnitUseCase) {
        this.canDeregisterUnitUseCase = canDeregisterUnitUseCase;
    }

    private final CanDeregisterUnitUseCase canDeregisterUnitUseCase;
}
