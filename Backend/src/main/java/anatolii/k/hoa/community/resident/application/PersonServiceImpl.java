package anatolii.k.hoa.community.resident.application;

import anatolii.k.hoa.community.person.application.PersonExistsUseCase;
import anatolii.k.hoa.community.resident.domain.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Override
    public boolean doesPersonExist(Long id) {
        return personExistsUseCase.doesPersonExist(id);
    }

    public PersonServiceImpl(PersonExistsUseCase personExistsUseCase) {
        this.personExistsUseCase = personExistsUseCase;
    }

    private final PersonExistsUseCase personExistsUseCase;
}
