package integration_test.community.units;

import anatolii.k.hoa.HoaApplication;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.person.internal.application.PersonDTO;
import anatolii.k.hoa.community.person.internal.application.RegisterPersonUseCase;
import anatolii.k.hoa.community.person.internal.domain.Person;
import anatolii.k.hoa.community.resident.internal.application.RegisterResidentUseCase;
import anatolii.k.hoa.community.resident.internal.application.ResidentException;
import anatolii.k.hoa.community.resident.internal.domain.ResidentRecord;
import anatolii.k.hoa.community.unit.internal.application.RegisterUnitUseCase;
import anatolii.k.hoa.community.unit.internal.domain.Unit;
import anatolii.k.hoa.community.unit.internal.application.UnitException;
import anatolii.k.hoa.community.unit.internal.application.UnitRepository;
import anatolii.k.hoa.security.Roles;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest(classes = HoaApplication.class,
                webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UnitsEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RegisterUnitUseCase registerUnitUseCase;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private RegisterPersonUseCase registerPersonUseCase;
    @Autowired
    private RegisterResidentUseCase registerResidentUseCase;
    @Autowired
    private ObjectMapper json;

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenCreateNewUnitWithUniqueNumber_thenOk() throws Exception {

        MvcResult response = mockMvc.perform( post("/api/units")
                        .content("""
                                { "number": "A001", "area": 40 }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect( status().isCreated() )
                .andReturn();

        String newUnitUrl = response.getResponse().getHeader("Location");
        assertThat(newUnitUrl).isNotBlank();

        response = mockMvc.perform( get(newUnitUrl).accept(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andExpect( content().contentType(MediaType.APPLICATION_JSON) )
                .andReturn();

        Unit newUnit = json.readValue( response.getResponse().getContentAsString(), Unit.class );

        assertThat(newUnit.number()).isEqualTo("A001");
        assertThat(newUnit.area()).isEqualTo(40);
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenCreateNewUnitWithNonUniqueNumber_thenFails() throws Exception {

        createUnit("A001", 60);

        MvcResult response = mockMvc.perform( post("/api/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         { "number": "A001", "area": 40 }
                         """))
                .andExpect( status().isUnprocessableEntity())
                .andReturn();

        var responseBody = json.readValue( response.getResponse().getContentAsString(), UseCaseResponse.class );

        assertThat(responseBody.ok()).isFalse();
        assertThat(responseBody.errorCode()).isEqualTo(UnitException.ErrorCode.ALREADY_EXISTS.toString());
        assertThat(responseBody.errorDetails()).isNotBlank();
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void givenNoUnitsInDB_whenGetAllUnits_thenReturnsEmptyList() throws Exception {
            MvcResult response = mockMvc.perform(get("/api/units")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            List<Unit> result = json.readValue(response.getResponse().getContentAsString(),
                    new TypeReference<List<Unit>>() {});

            assertThat(result).isEmpty();
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void givenSomeUnitsInDB_whenGetAllUnits_thenReturnsAllUnits() throws Exception {
        Unit unit1 = createUnit("AOO1", 40);
        Unit unit2 = createUnit("AOO2", 80);

        MvcResult response = mockMvc.perform( get("/api/units")
                        .accept(MediaType.APPLICATION_JSON) )
                .andExpect( status().isOk() )
                .andReturn();

        List<Unit> result = json.readValue(response.getResponse().getContentAsString(),
                new TypeReference<List<Unit>>() {});

        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(unit1, unit2);
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenGetExistingUnit_thenOk() throws Exception{
        Unit unit = createUnit("AOO1", 40);

        MvcResult response = mockMvc.perform( get("/api/units/{id}", unit.id())
                        .accept(MediaType.APPLICATION_JSON) )
                .andExpect( status().isOk() )
                .andReturn();

        Unit resultUnit = json.readValue( response.getResponse().getContentAsString(), Unit.class);

        assertThat(resultUnit).isEqualTo(unit);
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenGetNotExistingUnit_thenNotFound() throws Exception{

        MvcResult response = mockMvc.perform( get("/api/units/{id}", 777)
                        .accept(MediaType.APPLICATION_JSON) )
                .andExpect( status().isNotFound() )
                .andReturn();

        assertThat( response.getResponse().getContentAsString()).isBlank();
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenDeleteExistingUnitWithoutResidents_thenOk() throws Exception {
        Unit unit = createUnit("A001", 60);

        mockMvc.perform( delete("/api/units/{id}", unit.id()))
                .andExpect( status().isNoContent());

        boolean doesUnitExist = unitRepository.doesUnitExist(unit.id());
        assertThat(doesUnitExist).isFalse();
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenDeleteExistingUnitWithResident_thenFails() throws Exception{
        Unit unit = createUnit("A001", 60);
        Person person = createPerson("12334567890");
        registerResident(person, unit);

        MvcResult response = mockMvc.perform( delete("/api/units/{id}", unit.id()))
                .andExpect( status().isUnprocessableEntity())
                .andReturn();

        var responseBody = json.readValue( response.getResponse().getContentAsString(), UseCaseResponse.class );

        assertThat(responseBody.ok()).isFalse();
        assertThat(responseBody.errorCode()).isEqualTo(ResidentException.ErrorCode.UNIT_HAS_RESIDENTS.toString() );
        assertThat(responseBody.errorDetails()).isNotBlank();
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void whenDeleteNotExistingUnit_thenFails() throws Exception {

        MvcResult response = mockMvc.perform( delete("/api/units/111"))
                .andExpect( status().isUnprocessableEntity())
                .andReturn();

        var responseBody = json.readValue( response.getResponse().getContentAsString(), UseCaseResponse.class );

        assertThat(responseBody.ok()).isFalse();
        assertThat(responseBody.errorCode()).isEqualTo( UnitException.ErrorCode.NOT_EXISTS.toString() );
        assertThat(responseBody.errorDetails()).isNotBlank();
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.USER)
    void givenUserWithUserRole_whenGetExistingUnit_thenOk() throws Exception{
        Unit unit = createUnit("AOO1", 40);

        MvcResult response = mockMvc.perform( get("/api/units/{id}", unit.id())
                        .accept(MediaType.APPLICATION_JSON) )
                .andExpect( status().isOk() )
                .andReturn();

        Unit resultUnit = json.readValue( response.getResponse().getContentAsString(), Unit.class);

        assertThat(resultUnit).isEqualTo(unit);
    }

    @Test
    @Transactional
    //@WithMockUser(roles = Roles.USER)
    void givenUnauthorizedUser_whenGetExistingUnit_thenNotAllowed() throws Exception{
        Unit unit = createUnit("AOO1", 40);

        mockMvc.perform( get("/api/units/{id}", unit.id())
                        .accept(MediaType.APPLICATION_JSON) )
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.USER)
    void givenUserWithUserRole_whenCreateNewUnit_thenNotAllowed() throws Exception {

        mockMvc.perform( post("/api/units")
                        .content("""
                                { "number": "A001", "area": 40 }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect( status().isForbidden() );
    }


    /////////////////////////////////////////////////////////////

    private Unit createUnit(String number, Integer square ){
        var response = registerUnitUseCase.register( number, square );
        if(!response.ok()){
            throw new RuntimeException(response.errorDetails());
        }
        return response.data();
    }

    private Person createPerson( String ssn ){
        var response = registerPersonUseCase.register( new PersonDTO(null, "Fname", "Lname",
                "+380630001122", "test@gmail.com", ssn));
        if(!response.ok()){
            throw new RuntimeException(response.errorDetails());
        }
        return response.data();
    }

    private void registerResident(Person person, Unit unit){
        var response = registerResidentUseCase.register( new ResidentRecord(null, person.getId(), unit.id(), null));
        if(!response.ok()){
            throw new RuntimeException(response.errorDetails());
        }
    }

}
