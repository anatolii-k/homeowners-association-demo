package anatolii.k.hoa.community.unit.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.unit.internal.domain.Unit;

@UseCase
public class RegisterUnitUseCase {

    public UseCaseResponse<Unit> register(String unitNumber, Integer unitArea ) {
        return UseCaseProcessor.process(
                ()-> registerImpl(unitNumber, unitArea)
        );
    }

    private Unit registerImpl( String unitNumber, Integer unitArea ){
        if(unitRepository.doesUnitExist(unitNumber)){
            throw UnitException.alreadyExists(unitNumber);
        }
        return unitRepository.save( new Unit(null, unitNumber, unitArea) );
    }

    public RegisterUnitUseCase(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    private final UnitRepository unitRepository;
}
