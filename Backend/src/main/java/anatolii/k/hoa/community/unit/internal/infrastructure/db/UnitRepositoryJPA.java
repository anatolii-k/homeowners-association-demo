package anatolii.k.hoa.community.unit.internal.infrastructure.db;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface UnitRepositoryJPA extends CrudRepository<UnitEntity,Long> {

    boolean existsByNumber(String number);
    boolean existsById(Long id);
    Optional<UnitEntity> findByNumber(String number);
}
