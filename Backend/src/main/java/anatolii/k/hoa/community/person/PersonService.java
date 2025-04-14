package anatolii.k.hoa.community.person;

import anatolii.k.hoa.community.person.internal.application.PersonException;
import anatolii.k.hoa.community.person.internal.application.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    public void checkPersonExists(Long id){
        if(id == null){
            throw PersonException.notExists(0L);
        }
        if(!personRepository.existsPersonWithId(id)){
            throw PersonException.notExists(id);
        }
    }

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private final PersonRepository personRepository;
}
