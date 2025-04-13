package anatolii.k.hoa.community.unit.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.unit.internal.domain.Unit;
import anatolii.k.hoa.community.unit.internal.domain.RegisterUnitOperation;

@UseCase
public class RegisterUnitUseCase {

    public UseCaseResponse<Unit> register(String unitNumber, Integer unitSquare ) {
        return UseCaseProcessor.process(
                ()-> registerUnitOperation.register(unitNumber, unitSquare)
        );
    }

    public RegisterUnitUseCase(RegisterUnitOperation registerUnitOperation) {
        this.registerUnitOperation = registerUnitOperation;
    }

    private final RegisterUnitOperation registerUnitOperation;
}
