package anatolii.k.hoa.community.person.internal.application;

import anatolii.k.hoa.community.person.internal.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    List<Person> getAllPersons();
    Optional<Person> getPerson(Long id);

    boolean existsPersonWithSSN(String ssn);
    boolean existsPersonWithId(Long id);

    Person save(Person newPerson);
}
