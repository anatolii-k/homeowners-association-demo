package anatolii.k.hoa.community.members.infrastructure.db.internal;

import jakarta.persistence.*;

@Entity
@Table(name = "resident")
public class ResidentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long unitId;
}
