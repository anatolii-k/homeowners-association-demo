package integration_test.community.person;

import anatolii.k.hoa.HoaApplication;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.person.application.RegisterPersonUseCase;
import anatolii.k.hoa.community.person.domain.PersonException;
import anatolii.k.hoa.community.person.domain.RegisterPersonRequest;
import anatolii.k.hoa.community.person.infrastructure.dto.PersonDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest( classes = HoaApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class PersonEndpointTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper json;
    @Autowired
    RegisterPersonUseCase registerPersonUseCase;

    @Test
    @Transactional
    void whenRegisterNewPersonWithAllCorrectData_thenOk() throws Exception {

        var response = mockMvc.perform( post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "firstName" : "Fname",
                          "lastName" : "Lname",
                          "phoneNumber" : "+380633003033",
                          "email" : "person@gmail.com",
                          "ssn" : "1234567890"
                        }
                        """)
        ).andExpect( status().isCreated() )
         .andReturn();

        String newUnitUrl = response.getResponse().getHeader("Location");
        assertThat(newUnitUrl).isNotBlank();

        response = mockMvc.perform( get(newUnitUrl).accept(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andExpect( content().contentType(MediaType.APPLICATION_JSON) )
                .andReturn();

        PersonDTO newPerson = json.readValue( response.getResponse().getContentAsString(), PersonDTO.class );

        assertThat(newPerson.getFirstName()).isEqualTo("Fname");
        assertThat(newPerson.getLastName()).isEqualTo("Lname");
        assertThat(newPerson.getPhoneNumber()).isEqualTo("+380633003033");
        assertThat(newPerson.getEmail()).isEqualTo("person@gmail.com");
        assertThat(newPerson.getSsn()).isEqualTo("1234567890");
    }

    @Test
    @Transactional
    void whenRegisterNewPersonWithExistingSSN_thenFails() throws Exception {

        registerPersonUseCase.register( new RegisterPersonRequest("Fname2", "Lname2",
                "+380933003011", "person2@gmail.com", "1234567890"));

        var response = mockMvc.perform( post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        { "firstName" : "Fname",
                          "lastName" : "Lname",
                          "phoneNumber" : "+380633003033",
                          "email" : "person@gmail.com",
                          "ssn" : "1234567890"
                        }
                        """)
                ).andExpect( status().isUnprocessableEntity() )
                .andReturn();

        var responseBody = json.readValue( response.getResponse().getContentAsString(), UseCaseResponse.class );

        assertThat(responseBody.ok()).isFalse();
        assertThat(responseBody.errorCode()).isEqualTo(PersonException.ErrorCode.SSN_ALREADY_EXISTS.toString());
        assertThat(responseBody.errorDetails()).isNotBlank();
    }

    @Test
    @Transactional
    void whenGetAllPersons_thenReturnListWithAllPersons() throws Exception {

        PersonDTO person1 = new PersonDTO( null, "Fname1", "Lname1",
                "+380933003011", "person1@gmail.com", "1234567891" );

        PersonDTO person2 = new PersonDTO( null, "Fname2", "Lname2",
                "+380933003012", "person2@gmail.com", "1234567892" );

        registerPerson(person1);
        registerPerson(person2);

        var response = mockMvc.perform( get("/api/person").accept(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andReturn();

        List<PersonDTO> persons = json.readValue(response.getResponse().getContentAsString(),
                new TypeReference<List<PersonDTO>>() {});

        Condition<PersonDTO> conditionsPerson1 = getEqualsCondition(person1);
        Condition<PersonDTO> conditionsPerson2 = getEqualsCondition(person2);

        assertThat(persons)
                .hasSize(2)
                .haveAtLeastOne(conditionsPerson1)
                .haveAtLeastOne(conditionsPerson2);
    }

    private void registerPerson( PersonDTO person ){
        registerPersonUseCase.register( new RegisterPersonRequest(person.getFirstName(), person.getLastName(),
                person.getPhoneNumber(), person.getEmail(), person.getSsn()));
    }

    private Condition<PersonDTO> getEqualsCondition( PersonDTO arg ){
        return new Condition<>(
                        person -> person.getFirstName().equals(arg.getFirstName()) &&
                        person.getLastName().equals(arg.getLastName()) &&
                        person.getPhoneNumber().equals(arg.getPhoneNumber()) &&
                        person.getEmail().equals(arg.getEmail()) &&
                        person.getSsn().equals(arg.getSsn()),
                "condition for " + arg.getFirstName() );
    }


}
