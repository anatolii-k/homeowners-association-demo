package anatolii.k.hoa.community.person.internal.infrastructure.db;

import anatolii.k.hoa.community.person.internal.application.PersonDTO;
import org.springframework.data.repository.CrudRepository;

interface PersonRepositoryJPA extends CrudRepository<PersonDTO,Long> {
    boolean existsBySsn(String ssn);
}
