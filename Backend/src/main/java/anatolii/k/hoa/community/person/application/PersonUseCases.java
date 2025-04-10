package anatolii.k.hoa.community.person.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.person.domain.Person;
import anatolii.k.hoa.community.person.domain.PersonRepository;
import anatolii.k.hoa.community.person.domain.RegisterNewPersonOperation;

import java.util.List;
import java.util.Optional;

@UseCase
public class PersonUseCases {

    public List<Person> getAllPersons(){
        return personRepository.getAllPersons();
    }

    public Optional<Person> getPerson(Long id) {
        return personRepository.getPerson(id);
    }

    public UseCaseResponse<Person> registerNewPerson(String firstName,
                                                     String lastName,
                                                     String phoneNumber,
                                                     String email,
                                                     String ssn) {
        return UseCaseProcessor.process(
                ()-> newPersonOperation.register(firstName, lastName, phoneNumber, email, ssn)
        );
    }

    public PersonUseCases(PersonRepository personRepository) {
        this.personRepository = personRepository;
        this.newPersonOperation = new RegisterNewPersonOperation(personRepository);
    }

    private final PersonRepository personRepository;
    private final RegisterNewPersonOperation newPersonOperation;
}
