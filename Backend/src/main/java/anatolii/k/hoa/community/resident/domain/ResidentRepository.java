package anatolii.k.hoa.community.resident.domain;

public interface ResidentRepository {
    ResidentRecord save(ResidentRecord residentRecord);

    boolean hasResidentsInUnit(Long unitId);
}
