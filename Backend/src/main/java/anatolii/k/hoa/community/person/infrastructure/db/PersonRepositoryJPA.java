package anatolii.k.hoa.community.person.infrastructure.db;

import anatolii.k.hoa.community.person.infrastructure.dto.PersonDTO;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepositoryJPA extends CrudRepository<PersonDTO,Long> {
    boolean existsBySsn(String ssn);
}
