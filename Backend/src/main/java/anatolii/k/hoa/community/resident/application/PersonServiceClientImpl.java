package anatolii.k.hoa.community.resident.application;

import anatolii.k.hoa.community.person.api.PersonService;
import anatolii.k.hoa.community.resident.domain.PersonServiceClient;
import org.springframework.stereotype.Component;

@Component
public class PersonServiceClientImpl implements PersonServiceClient {

    @Override
    public boolean doesPersonExist(Long id) {
        return personService.doesPersonExist(id);
    }

    public PersonServiceClientImpl(PersonService personService) {
        this.personService = personService;
    }

    private final PersonService personService;
}
