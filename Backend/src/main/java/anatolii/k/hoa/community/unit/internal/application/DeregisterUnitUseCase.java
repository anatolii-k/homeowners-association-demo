package anatolii.k.hoa.community.unit.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.unit.BeforeDeregisterUnitEvent;
import anatolii.k.hoa.community.unit.internal.domain.DeregisterUnitOperation;
import org.springframework.context.ApplicationEventPublisher;

@UseCase
public class DeregisterUnitUseCase {

    public UseCaseResponse<Void> deregister(Long id ){
        return UseCaseProcessor.process(
                ()-> {
                    eventPublisher.publishEvent(new BeforeDeregisterUnitEvent(id));
                    deregisterUnitOperation.deregister(id);
                }
        );
    }

    public DeregisterUnitUseCase(ApplicationEventPublisher eventPublisher, DeregisterUnitOperation deregisterUnitOperation) {
        this.eventPublisher = eventPublisher;
        this.deregisterUnitOperation = deregisterUnitOperation;
    }

    private final ApplicationEventPublisher eventPublisher;
    private final DeregisterUnitOperation deregisterUnitOperation;
}
