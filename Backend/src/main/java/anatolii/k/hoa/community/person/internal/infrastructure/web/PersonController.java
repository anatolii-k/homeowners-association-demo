package anatolii.k.hoa.community.person.internal.infrastructure.web;

import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.person.internal.application.RegisterPersonUseCase;
import anatolii.k.hoa.community.person.internal.domain.Person;
import anatolii.k.hoa.community.person.internal.domain.PersonRepository;
import anatolii.k.hoa.community.person.internal.domain.RegisterPersonRequest;
import anatolii.k.hoa.community.person.internal.infrastructure.dto.PersonDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/person")
public class PersonController {

    @GetMapping
    List<PersonDTO> getAllPersons(){
        return personRepository.getAllPersons()
                .stream()
                .map(PersonDTO::fromDomain)
                .toList();
    }

    @GetMapping("{id}")
    ResponseEntity<PersonDTO> getPerson( @PathVariable Long id ){
        Optional<Person> person = personRepository.getPerson(id);
        if(person.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PersonDTO.fromDomain(person.get()));
    }

    @PostMapping
    ResponseEntity<UseCaseResponse<Person>> registerPerson(@RequestBody RegisterPersonRequest registerPersonRequest,
                                                           UriComponentsBuilder uriBuilder){
        UseCaseResponse<Person> useCaseResponse = registerPersonUseCase.register(registerPersonRequest);
        if(useCaseResponse.ok()){
            URI personUri = uriBuilder.path("api/person/{id}")
                    .buildAndExpand(useCaseResponse.data().getId())
                    .toUri();
            return ResponseEntity.created(personUri).build();
        }
        return ResponseEntity.unprocessableEntity().body(useCaseResponse);
    }

    public PersonController(RegisterPersonUseCase registerPersonUseCase, PersonRepository personRepository) {
        this.registerPersonUseCase = registerPersonUseCase;
        this.personRepository = personRepository;
    }

    private final RegisterPersonUseCase registerPersonUseCase;
    private final PersonRepository personRepository;
}
