package anatolii.k.hoa.community.resident.internal.infrastructure.db;

import anatolii.k.hoa.community.resident.internal.domain.ResidentRecord;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "resident_record")
class ResidentRecordDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="person_id")
    private Long personId;

    @Column(name="unit_id")
    private Long unitId;

    @Column(name="registered_at")
    private LocalDate registeredAt;

    static public ResidentRecordDTO fromDomain(ResidentRecord domain){
        return new ResidentRecordDTO(domain.getId(), domain.getPersonId(), domain.getUnitId(), domain.getRegisteredAt());
    }

    public ResidentRecord toDomain(){
        return new ResidentRecord(id, personId, unitId, registeredAt);
    }

    public ResidentRecordDTO() {
    }

    public ResidentRecordDTO(Long id, Long personId, Long unitId, LocalDate registeredAt) {
        this.id = id;
        this.personId = personId;
        this.unitId = unitId;
        this.registeredAt = registeredAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public LocalDate getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDate registeredAt) {
        this.registeredAt = registeredAt;
    }
}

