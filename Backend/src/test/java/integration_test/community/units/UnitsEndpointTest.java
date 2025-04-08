package integration_test.community.units;

import anatolii.k.hoa.HoaApplication;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.units.application.GetUnitsUseCases;
import anatolii.k.hoa.community.units.application.UnitRegistrationUseCases;
import anatolii.k.hoa.community.units.domain.Unit;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    private UnitRegistrationUseCases unitRegistrationUseCases;
    @Autowired
    private GetUnitsUseCases getUnitsUseCases;
    @Autowired
    private ObjectMapper json;

    @Test
    @Transactional
    void whenCreateNewUnitWithUniqueNumber_thenOk() throws Exception {

        MvcResult response = mockMvc.perform( post("/api/units")
                        .content("""
                                { "number": "A001", "square": 40 }
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
        assertThat(newUnit.square()).isEqualTo(40);
    }

    @Test
    @Transactional
    void whenCreateNewUnitWithNonUniqueNumber_thenFails() throws Exception {

        createUnit("A001", 60);

        MvcResult response = mockMvc.perform( post("/api/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         { "number": "A001", "square": 40 }
                         """))
                .andExpect( status().isUnprocessableEntity())
                .andReturn();

        UseCaseResponse responseBody = json.readValue( response.getResponse().getContentAsString(), UseCaseResponse.class );

        assertThat(responseBody.ok()).isFalse();
        assertThat(responseBody.error()).isNotBlank();
    }

    @Test
    @Transactional
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
    void whenGetNotExistingUnit_thenNotFound() throws Exception{

        MvcResult response = mockMvc.perform( get("/api/units/{id}", 777)
                        .accept(MediaType.APPLICATION_JSON) )
                .andExpect( status().isNotFound() )
                .andReturn();

        assertThat( response.getResponse().getContentAsString()).isBlank();
    }

    @Test
    @Transactional
    void whenDeleteExistingUnit_thenOk() throws Exception {
        Unit unit = createUnit("A001", 60);

        mockMvc.perform( delete("/api/units/{id}", unit.id()))
                .andExpect( status().isNoContent());

        boolean doesUnitExist = getUnitsUseCases.getUnit(unit.id()).isPresent();
        assertThat(doesUnitExist).isFalse();
    }

    @Test
    @Transactional
    void whenDeleteNotExistingUnit_thenFails() throws Exception {

        MvcResult response = mockMvc.perform( delete("/api/units/111"))
                .andExpect( status().isUnprocessableEntity())
                .andReturn();

        UseCaseResponse responseBody = json.readValue( response.getResponse().getContentAsString(), UseCaseResponse.class );

        assertThat(responseBody.ok()).isFalse();
        assertThat(responseBody.error()).isNotBlank();
    }


    private Unit createUnit(String number, Integer square ){
        var response = unitRegistrationUseCases.register( number, square );
        if(!response.ok()){
            throw new RuntimeException(response.error());
        }
        return response.data();
    }

}
