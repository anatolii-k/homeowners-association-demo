package anatolii.k.hoa.community.units.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.units.domain.Unit;
import anatolii.k.hoa.community.units.domain.UnitRegistrationOperations;
import anatolii.k.hoa.community.units.domain.UnitRepository;

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

    public UnitRegistrationUseCases(UnitRepository unitRepository) {
        this.registrationOperations = new UnitRegistrationOperations(unitRepository);
    }

    private final UnitRegistrationOperations registrationOperations;
}
