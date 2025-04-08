package anatolii.k.hoa.community.units.domain;

import java.util.List;
import java.util.Optional;

public interface UnitRepository {

    List<Unit> getAllUnits();
    Optional<Unit> getUnitById(Long id);

    boolean doesUnitExist( String number );
    boolean doesUnitExist( Long id );
    boolean hasUnitResidents(Long id);

    Unit save(Unit unit);

    void delete(Long unitId);
}
