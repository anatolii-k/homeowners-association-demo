package anatolii.k.hoa.community.unit.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.unit.domain.Unit;
import anatolii.k.hoa.community.unit.domain.RegisterUnitOperation;

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
