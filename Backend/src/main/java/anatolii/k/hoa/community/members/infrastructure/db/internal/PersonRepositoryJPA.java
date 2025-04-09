package anatolii.k.hoa.community.members.infrastructure.db.internal;

import anatolii.k.hoa.community.members.infrastructure.dto.PersonDTO;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepositoryJPA extends CrudRepository<PersonDTO,Long> {
    boolean existsBySsn(String ssn);
}
