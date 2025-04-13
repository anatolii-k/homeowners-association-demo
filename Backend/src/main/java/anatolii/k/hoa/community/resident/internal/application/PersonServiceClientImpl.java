package anatolii.k.hoa.community.resident.internal.application;

import anatolii.k.hoa.community.person.PersonService;
import anatolii.k.hoa.community.resident.internal.domain.PersonServiceClient;
import org.springframework.stereotype.Component;

@Component
public class PersonServiceClientImpl implements PersonServiceClient {

    @Override
    public void checkPersonExists(Long id) {
        personService.checkPersonExists(id);
    }
    public PersonServiceClientImpl(PersonService personService) {
        this.personService = personService;
    }

    private final PersonService personService;

}
