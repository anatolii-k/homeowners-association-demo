package anatolii.k.hoa.community.person.api;

import anatolii.k.hoa.community.person.domain.PersonRepository;

public class PersonService {

    public boolean doesPersonExist(Long id){
        return personRepository.existsPersonWithId(id);
    }

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private final PersonRepository personRepository;
}
