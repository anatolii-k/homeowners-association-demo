package anatolii.k.hoa.community.person.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.community.person.domain.PersonRepository;

@UseCase
public class PersonExistsUseCase {
    public boolean doesPersonExist(Long id){
        return personRepository.existsPersonWithId(id);
    }

    public PersonExistsUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private final PersonRepository personRepository;
}
