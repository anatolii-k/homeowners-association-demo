package integration_test.community.person;

import anatolii.k.hoa.HoaApplication;
import anatolii.k.hoa.common.application.AttributeValidators;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.person.internal.application.RegisterPersonUseCase;
import anatolii.k.hoa.community.person.internal.application.PersonException;
import anatolii.k.hoa.community.person.internal.application.PersonDTO;
import anatolii.k.hoa.security.Roles;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest( classes = HoaApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource( properties = {
        "hoa.phone-number.country-code.default=99"
})
public class PersonEndpointTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper json;
    @Autowired
    RegisterPersonUseCase registerPersonUseCase;

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenRegisterNewPersonWithAllCorrectData_thenOk() throws Exception {

        var response = mockMvc.perform( post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "firstName" : "Fname",
                          "lastName" : "Lname",
                          "phoneNumber" : "(063)300 30 33",
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
        assertThat(newPerson.getPhoneNumber()).isEqualTo("990633003033"); // check that default country code was applied
        assertThat(newPerson.getEmail()).isEqualTo("person@gmail.com");
        assertThat(newPerson.getSsn()).isEqualTo("1234567890");
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenRegisterNewPersonWithExistingSSN_thenFails() throws Exception {

        registerPersonUseCase.register( new PersonDTO(null, "Fname2", "Lname2",
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
    @WithMockUser(roles = Roles.ADMIN)
    void whenRegisterNewPersonWithoutFirstName_thenFails() throws Exception {

        registerPersonUseCase.register( new PersonDTO(null, "Fname2", "Lname2",
                "+380933003011", "person2@gmail.com", "1234567890"));

        var response = mockMvc.perform( post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
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
        assertThat(responseBody.errorCode()).isEqualTo(AttributeValidators.ErrorCode.EMPTY_VALUE.toString());
        assertThat(responseBody.errorDetails()).isNotBlank();
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenGetAllPersons_thenReturnListWithAllPersons() throws Exception {

        PersonDTO person1 = new PersonDTO( null, "Fname1", "Lname1",
                "380933003011", "person1@gmail.com", "1234567891" );

        PersonDTO person2 = new PersonDTO( null, "Fname2", "Lname2",
                "380933003012", "person2@gmail.com", "1234567892" );

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

    @Test
    @Transactional
    @WithMockUser(roles = Roles.USER)
    void givenUserWithUserRole_whenGetAllPersons_thenNotAllowed() throws Exception {

        PersonDTO person1 = new PersonDTO( null, "Fname1", "Lname1",
                "380933003011", "person1@gmail.com", "1234567891" );

        PersonDTO person2 = new PersonDTO( null, "Fname2", "Lname2",
                "380933003012", "person2@gmail.com", "1234567892" );

        registerPerson(person1);
        registerPerson(person2);

        var response = mockMvc.perform( get("/api/person").accept(MediaType.APPLICATION_JSON) )
                .andExpect(status().isForbidden())
                .andReturn();
    }


    /// ///////////////////////////////////////////////////////////////////

    private void registerPerson( PersonDTO person ){
        registerPersonUseCase.register( new PersonDTO(null, person.getFirstName(), person.getLastName(),
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
