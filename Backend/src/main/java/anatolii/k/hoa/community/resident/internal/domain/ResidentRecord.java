package anatolii.k.hoa.community.resident.internal.domain;

import java.time.LocalDate;

public class ResidentRecord {
    private Long id;
    private Long personId;
    private Long unitId;
    private LocalDate registeredAt;

    public ResidentRecord(){}

    public ResidentRecord(Long id, Long personId, Long unitId, LocalDate registeredAt) {
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

    @Override
    public String toString() {
        return "ResidentRecord{" +
                "id=" + id +
                ", personId=" + personId +
                ", unitId=" + unitId +
                ", registeredAt=" + registeredAt +
                '}';
    }
}
