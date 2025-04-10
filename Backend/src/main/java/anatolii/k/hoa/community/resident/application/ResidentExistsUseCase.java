package anatolii.k.hoa.community.resident.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.community.resident.domain.ResidentRepository;

@UseCase
public class ResidentExistsUseCase {
    public boolean hasResidentsInUnit(Long unitId) {
        return residentRepository.hasResidentsInUnit(unitId);
    }

    public ResidentExistsUseCase(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    private final ResidentRepository residentRepository;
}
