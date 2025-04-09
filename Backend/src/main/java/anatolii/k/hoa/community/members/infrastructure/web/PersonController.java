package anatolii.k.hoa.community.members.infrastructure.web;

import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.members.application.PersonUseCases;
import anatolii.k.hoa.community.members.domain.Person;
import anatolii.k.hoa.community.members.infrastructure.dto.PersonDTO;
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
        return personUseCases.getAllPersons()
                .stream()
                .map(PersonDTO::fromDomain)
                .toList();
    }

    @GetMapping("{id}")
    ResponseEntity<PersonDTO> getPerson( @PathVariable Long id ){
        Optional<Person> person = personUseCases.getPerson(id);
        if(person.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PersonDTO.fromDomain(person.get()));
    }

    @PostMapping
    ResponseEntity<UseCaseResponse<Person>> registerNewPerson(@RequestBody PersonDTO person,
                                                              UriComponentsBuilder uriBuilder){
        UseCaseResponse<Person> useCaseResponse = personUseCases.registerNewPerson(
                person.getFirstName(), person.getLastName(), person.getPhoneNumber(),
                person.getEmail(), person.getSsn());
        if(useCaseResponse.ok()){
            URI personUri = uriBuilder.path("api/person/{id}")
                    .buildAndExpand(useCaseResponse.data().getId())
                    .toUri();
            return ResponseEntity.created(personUri).build();
        }
        return ResponseEntity.unprocessableEntity().body(useCaseResponse);
    }

    public PersonController(PersonUseCases personUseCases) {
        this.personUseCases = personUseCases;
    }

    private final PersonUseCases personUseCases;
}
