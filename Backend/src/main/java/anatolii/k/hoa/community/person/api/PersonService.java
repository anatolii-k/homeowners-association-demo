package anatolii.k.hoa.community.person.api;

import anatolii.k.hoa.community.person.domain.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    public boolean doesPersonExist(Long id){
        return personRepository.existsPersonWithId(id);
    }

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private final PersonRepository personRepository;
}
