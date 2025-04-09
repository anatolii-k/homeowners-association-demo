package anatolii.k.hoa.community.units.infrastructure.db.internal;

import anatolii.k.hoa.community.units.domain.Unit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UnitRepositoryJPA extends CrudRepository<UnitEntity,Long> {

    boolean existsByNumber(String number);
    boolean existsById(Long id);
    Optional<UnitEntity> findByNumber(String number);

    @Query("SELECT COUNT(1) != 0 FROM ResidentEntity r WHERE r.unitId = :unitId")
    boolean hasResidents(@Param("unitId") Long id);
}
