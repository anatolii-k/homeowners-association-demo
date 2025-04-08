package anatolii.k.hoa.community.units.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.units.domain.Unit;
import anatolii.k.hoa.community.units.domain.UnitRegistrationOperations;
import anatolii.k.hoa.community.units.domain.UnitRepository;

@UseCase
public class UnitRegistrationUseCases {

    public UseCaseResponse<Unit> register(String unitNumber, Integer unitSquare ) {

        var responseBuilder = UseCaseResponse.<Unit>builder();
        try{
            Unit newUnit = registrationOperations.register(unitNumber, unitSquare);
            responseBuilder.ok(true)
                           .data(newUnit);
        }
        catch(Throwable e){
            responseBuilder.ok(false)
                           .error(e.getMessage());
        }
        return responseBuilder.build();
    }

    public UseCaseResponse<Void> unregister(Long id ){
        var responseBuilder = UseCaseResponse.<Void>builder();
        try{
            registrationOperations.unregister(id);
            responseBuilder.ok(true);
        }
        catch (Throwable e){
            responseBuilder.ok(false)
                    .error(e.getMessage());
        }
        return responseBuilder.build();
    }

    public UnitRegistrationUseCases(UnitRepository unitRepository) {
        this.registrationOperations = new UnitRegistrationOperations(unitRepository);
    }

    private final UnitRegistrationOperations registrationOperations;
}
