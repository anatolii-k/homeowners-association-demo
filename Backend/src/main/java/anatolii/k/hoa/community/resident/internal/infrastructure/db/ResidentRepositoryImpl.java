package anatolii.k.hoa.community.resident.internal.infrastructure.db;

import anatolii.k.hoa.community.resident.internal.application.ResidentRepository;
import anatolii.k.hoa.community.resident.internal.domain.ResidentRecord;
import org.springframework.stereotype.Repository;

@Repository
public class ResidentRepositoryImpl implements ResidentRepository {
    @Override
    public ResidentRecord save(ResidentRecord residentRecord) {
        return residentRepositoryJPA.save(ResidentRecordDTO.fromDomain(residentRecord)).toDomain();
    }

    @Override
    public boolean hasResidentsInUnit(Long unitId) {
        return residentRepositoryJPA.existsByUnitId(unitId);
    }

    public ResidentRepositoryImpl(ResidentRepositoryJPA residentRepositoryJPA) {
        this.residentRepositoryJPA = residentRepositoryJPA;
    }

    private final ResidentRepositoryJPA residentRepositoryJPA;
}
