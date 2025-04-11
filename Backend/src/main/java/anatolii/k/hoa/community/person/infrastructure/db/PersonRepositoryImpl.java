package anatolii.k.hoa.community.person.infrastructure.db;

import anatolii.k.hoa.community.person.domain.PersonRepository;
import anatolii.k.hoa.community.person.domain.Person;
import anatolii.k.hoa.community.person.infrastructure.dto.PersonDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    @Override
    public List<Person> getAllPersons() {
        return StreamSupport.stream( personRepositoryJPA.findAll().spliterator(), false )
                .map(PersonDTO::toDomain)
                .toList();
    }

    @Override
    public Optional<Person> getPerson(Long id) {
        return personRepositoryJPA.findById(id).map(PersonDTO::toDomain);
    }

    @Override
    public boolean existsPersonWithSSN(String ssn) {
        return personRepositoryJPA.existsBySsn(ssn);
    }

    @Override
    public boolean existsPersonWithId(Long id) {
        return personRepositoryJPA.existsById(id);
    }

    @Override
    public Person save(Person newPerson) {
        return personRepositoryJPA.save(PersonDTO.fromDomain(newPerson)).toDomain();
    }

    public PersonRepositoryImpl(PersonRepositoryJPA personRepositoryJPA) {
        this.personRepositoryJPA = personRepositoryJPA;
    }

    private final PersonRepositoryJPA  personRepositoryJPA;
}
