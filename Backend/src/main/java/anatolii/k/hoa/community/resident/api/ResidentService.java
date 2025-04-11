package anatolii.k.hoa.community.resident.api;

import anatolii.k.hoa.community.resident.domain.ResidentRepository;
import org.springframework.stereotype.Service;

@Service
public class ResidentService {

    public boolean hasResidentsInUnit(Long unitId) {
        return residentRepository.hasResidentsInUnit(unitId);
    }

    public ResidentService(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    private final ResidentRepository residentRepository;
}
