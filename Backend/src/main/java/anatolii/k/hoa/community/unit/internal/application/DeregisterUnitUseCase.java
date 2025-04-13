package anatolii.k.hoa.community.unit.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.unit.internal.domain.DeregisterUnitOperation;

@UseCase
public class DeregisterUnitUseCase {

    public UseCaseResponse<Void> deregister(Long id ){
        return UseCaseProcessor.process(
                ()-> deregisterUnitOperation.deregister(id)
        );
    }

    public DeregisterUnitUseCase(DeregisterUnitOperation deregisterUnitOperation) {
        this.deregisterUnitOperation = deregisterUnitOperation;
    }

    private final DeregisterUnitOperation deregisterUnitOperation;
}
