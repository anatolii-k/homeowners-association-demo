package anatolii.k.hoa.community.resident.internal.infrastructure.db;

import org.springframework.data.repository.CrudRepository;

interface ResidentRepositoryJPA extends CrudRepository<ResidentRecordEntity,Long> {
    boolean existsByUnitId(Long id);
}
