package anatolii.k.hoa.community.resident.internal.domain;

public interface ResidentRepository {
    ResidentRecord save(ResidentRecord residentRecord);

    boolean hasResidentsInUnit(Long unitId);
}
