package anatolii.k.hoa.community.resident.infrastructure.db.internal;

import org.springframework.data.repository.CrudRepository;

public interface ResidentRepositoryJPA extends CrudRepository<ResidentRecordEntity,Long> {
    boolean existsByUnitId(Long id);
}
