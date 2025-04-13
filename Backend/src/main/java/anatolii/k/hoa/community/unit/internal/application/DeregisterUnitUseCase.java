package anatolii.k.hoa.community.unit.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.unit.BeforeDeregisterUnitEvent;
import org.springframework.context.ApplicationEventPublisher;

@UseCase
public class DeregisterUnitUseCase {

    public UseCaseResponse<Void> deregister(Long id ){
        return UseCaseProcessor.process(
                ()-> deregisterImpl(id)
        );
    }

    public void deregisterImpl(Long id ){
        if(!unitRepository.doesUnitExist(id)){
            throw UnitException.notExists(id);
        }
        eventPublisher.publishEvent(new BeforeDeregisterUnitEvent(id));

        unitRepository.delete( id );
    }


    public DeregisterUnitUseCase(ApplicationEventPublisher eventPublisher, UnitRepository unitRepository) {
        this.eventPublisher = eventPublisher;
        this.unitRepository = unitRepository;
    }

    private final ApplicationEventPublisher eventPublisher;
    private final UnitRepository unitRepository;
}
