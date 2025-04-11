package anatolii.k.hoa.community.resident.application;

import anatolii.k.hoa.community.resident.domain.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Override
    public boolean doesPersonExist(Long id) {
        return personService.doesPersonExist(id);
    }

    public PersonServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    private final PersonService personService;
}
