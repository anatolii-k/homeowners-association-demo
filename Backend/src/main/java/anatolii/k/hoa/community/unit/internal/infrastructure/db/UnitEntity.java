package anatolii.k.hoa.community.unit.internal.infrastructure.db;

import anatolii.k.hoa.community.unit.internal.domain.Unit;
import jakarta.persistence.*;

@Entity
@Table(name = "unit")
class UnitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String number;
    Integer area;

    public static UnitEntity fromDomain(Unit domainUnit){
        return new UnitEntity(domainUnit.id(), domainUnit.number(), domainUnit.area());
    }

    public Unit toDomain(){
        return new Unit(id, number, area);
    }

    public UnitEntity(Long id, String number, Integer area) {
        this.id = id;
        this.number = number;
        this.area = area;
    }

    public UnitEntity() {
        this(null, null, null);
    }
}
