package anatolii.k.hoa.community.unit.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.unit.domain.Unit;
import anatolii.k.hoa.community.unit.domain.UnitRegistrationOperations;

@UseCase
public class UnitRegistrationUseCases {

    public UseCaseResponse<Unit> register(String unitNumber, Integer unitSquare ) {
        return UseCaseProcessor.process(
                ()-> registrationOperations.register(unitNumber, unitSquare)
        );
    }

    public UseCaseResponse<Void> unregister(Long id ){
        return UseCaseProcessor.process(
                ()-> registrationOperations.unregister(id)
        );
    }

    public UnitRegistrationUseCases(UnitRegistrationOperations registrationOperations) {
        this.registrationOperations = registrationOperations;
    }

    private final UnitRegistrationOperations registrationOperations;
}
