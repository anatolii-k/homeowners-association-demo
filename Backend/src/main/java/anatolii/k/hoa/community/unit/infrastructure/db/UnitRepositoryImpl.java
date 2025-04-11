package anatolii.k.hoa.community.unit.infrastructure.db;

import anatolii.k.hoa.community.unit.domain.Unit;
import anatolii.k.hoa.community.unit.domain.UnitRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class UnitRepositoryImpl implements UnitRepository {

    public UnitRepositoryImpl(UnitRepositoryJPA jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Unit> getAllUnits() {
        return StreamSupport.stream( jpaRepository.findAll().spliterator(), false )
                .map( UnitEntity::toDomain )
                .toList();
    }

    @Override
    public Optional<Unit> getUnitById(Long id) {
        return jpaRepository.findById(id).map(UnitEntity::toDomain);
    }

    @Override
    public boolean doesUnitExist(String number) {
        return jpaRepository.existsByNumber(number);
    }

    @Override
    public boolean doesUnitExist(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Unit save(Unit unit) {
        return jpaRepository.save(UnitEntity.fromDomain(unit)).toDomain();
    }

    @Override
    public void delete(Long unitId) {
        jpaRepository.deleteById(unitId);
    }

    private final UnitRepositoryJPA jpaRepository;
}
