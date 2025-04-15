package anatolii.k.hoa.community.resident.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;

@UseCase
public class CanDeregisterUnitUseCase {

    public void check(Long unitId){
        if(residentRepository.hasResidentsInUnit(unitId)){
            throw ResidentException.unitHasResidents(unitId);
        }
    }

    public CanDeregisterUnitUseCase(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    private final ResidentRepository residentRepository;
}
