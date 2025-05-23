package integration_test.community.resident;

import anatolii.k.hoa.HoaApplication;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.person.internal.application.PersonDTO;
import anatolii.k.hoa.community.person.internal.application.RegisterPersonUseCase;
import anatolii.k.hoa.community.person.internal.domain.Person;
import anatolii.k.hoa.community.person.internal.application.PersonException;
import anatolii.k.hoa.community.unit.internal.application.RegisterUnitUseCase;
import anatolii.k.hoa.community.unit.internal.domain.Unit;
import anatolii.k.hoa.security.Roles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest( classes = HoaApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ResidentEndpointTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper json;

    @Autowired
    RegisterPersonUseCase registerPersonUseCase;
    @Autowired
    RegisterUnitUseCase registerUnitUseCase;

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenRegisterResidentWithCorrectData_thenOk() throws Exception {

        Unit unit = createUnit("010", 120);
        Person person = createPerson("1112223334");


        var response = mockMvc.perform( post("/api/resident")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "personId" : "%d", "unitId" : "%d" }
                        """.formatted(person.getId(), unit.id())))
                .andExpect(status().isCreated())
                .andReturn();

        String unitWithResidentsUri = response.getResponse().getHeader("Location");
        assertThat(unitWithResidentsUri).isNotBlank();

//        response = mockMvc.perform( get(unitWithResidentsUri)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenRegisterResidentWithNonExistingPerson_thenFails() throws Exception {

        Unit unit = createUnit("010", 120);

        var response = mockMvc.perform( post("/api/resident")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        { "personId" : "%d", "unitId" : "%d" }
                        """.formatted(111L, unit.id())))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        var responseBody = json.readValue( response.getResponse().getContentAsString(), UseCaseResponse.class );

        assertThat(responseBody.ok()).isFalse();
        assertThat(responseBody.errorCode()).isEqualTo(PersonException.ErrorCode.NOT_EXISTS.toString());
        assertThat(responseBody.errorDetails()).isNotBlank();
    }


    @Test
    @Transactional
    @WithMockUser(roles = Roles.USER)
    void givenUserWithUserRole_whenRegisterResident_thenNotAllowed() throws Exception {

        Unit unit = createUnit("010", 120);
        Person person = createPerson("1112223334");


        mockMvc.perform( post("/api/resident")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        { "personId" : "%d", "unitId" : "%d" }
                        """.formatted(person.getId(), unit.id())))
                .andExpect(status().isForbidden());
    }


/////////////////////////////////////////////////////////////

    private Unit createUnit(String number, Integer square ){
        var response = registerUnitUseCase.register( number, square );
        if(!response.ok()){
            throw new RuntimeException(response.errorDetails());
        }
        return response.data();
    }

    private Person createPerson(String ssn ){
        var response = registerPersonUseCase.register(
                new PersonDTO(null, "Fname", "Lname",
                "+380630001122", "test@gmail.com", ssn));
        if(!response.ok()){
            throw new RuntimeException(response.errorDetails());
        }
        return response.data();
    }

}
