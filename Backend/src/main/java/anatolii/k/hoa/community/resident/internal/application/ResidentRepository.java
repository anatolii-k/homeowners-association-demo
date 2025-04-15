package anatolii.k.hoa.community.resident.internal.application;

import anatolii.k.hoa.community.resident.internal.domain.ResidentRecord;

public interface ResidentRepository {
    ResidentRecord save(ResidentRecord residentRecord);

    boolean hasResidentsInUnit(Long unitId);
}
