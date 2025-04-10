package anatolii.k.hoa.community.person.domain;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    List<Person> getAllPersons();

    boolean existsPersonWithSSN(String ssn);

    Person save(Person newPerson);

    Optional<Person> getPerson(Long id);

    boolean existsPersonWithId(Long id);
}
