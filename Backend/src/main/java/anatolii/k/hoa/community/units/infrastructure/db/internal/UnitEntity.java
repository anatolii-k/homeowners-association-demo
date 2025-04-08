package anatolii.k.hoa.community.units.infrastructure.db.internal;

import anatolii.k.hoa.community.units.domain.Unit;
import jakarta.persistence.*;
//import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table(name = "unit")
public class UnitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String number;
    Integer square;

    public static UnitEntity fromDomain(Unit domainUnit){
        return new UnitEntity(domainUnit.id(), domainUnit.number(), domainUnit.square());
    }

    public Unit toDomain(){
        return new Unit(id, number, square);
    }

    public UnitEntity(Long id, String number, Integer square) {
        this.id = id;
        this.number = number;
        this.square = square;
    }

    public UnitEntity() {
        this(null, null, null);
    }
}
