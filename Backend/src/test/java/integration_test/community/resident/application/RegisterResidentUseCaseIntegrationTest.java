package integration_test.community.resident.application;

import anatolii.k.hoa.HoaApplication;
import anatolii.k.hoa.community.person.internal.application.PersonDTO;
import anatolii.k.hoa.community.person.internal.application.RegisterPersonUseCase;
import anatolii.k.hoa.community.person.internal.domain.Person;
import anatolii.k.hoa.community.resident.internal.application.RegisterResidentUseCase;
import anatolii.k.hoa.community.resident.internal.domain.ResidentRecord;
import anatolii.k.hoa.community.unit.internal.application.RegisterUnitUseCase;
import anatolii.k.hoa.community.unit.internal.domain.Unit;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest( classes = HoaApplication.class)
@TestPropertySource(
        properties = { "hoa.person.attributes.required=FIRST_NAME,SSN" }
)
public class RegisterResidentUseCaseIntegrationTest {

    @Autowired
    RegisterResidentUseCase registerResidentUseCase;

    @Autowired
    RegisterUnitUseCase registerUnitUseCase;

    @Autowired
    RegisterPersonUseCase registerPersonUseCase;

    @Test
    @Transactional
    void whenRegisteredAtIsNull_thenDefaultWithCurrentDate(){

        LocalDate currentDateBefore = LocalDate.now();


        Unit unit = registerUnitUseCase.register("B001", 50).data();
        Person person = registerPersonUseCase.register(
                new PersonDTO(null, "Fname", null, "", null, "111111111" ) )
                .data();


        var response = registerResidentUseCase.register( new ResidentRecord(null, person.getId(), unit.id(), null) );

        LocalDate currentDateAfter = LocalDate.now();

        assertThat(currentDateAfter)
                .as("Current date is changed during UT execution. Please rerun UT")
                .isEqualTo(currentDateBefore);

        assertThat(response.ok()).isTrue();
        assertThat(response.data().getRegisteredAt()).isEqualTo(currentDateBefore);
        assertThat(response.data().getUnitId()).isEqualTo(unit.id());
        assertThat(response.data().getPersonId()).isEqualTo(person.getId());
    }

    @Test
    @Transactional
    void whenRegisteredAtIsInPast_thenDefaultWithCurrentDate(){

        LocalDate registeredAt = LocalDate.now().minusDays(10);


        Unit unit = registerUnitUseCase.register("B001", 50).data();
        Person person = registerPersonUseCase.register(
                        new PersonDTO(null, "Fname", null, "", null, "111111111" ) )
                .data();

        var response = registerResidentUseCase.register( new ResidentRecord(
                null, person.getId(), unit.id(), registeredAt) );

        assertThat(response.ok()).isTrue();
        assertThat(response.data().getRegisteredAt()).isEqualTo(registeredAt);
        assertThat(response.data().getUnitId()).isEqualTo(unit.id());
        assertThat(response.data().getPersonId()).isEqualTo(person.getId());
    }
}
