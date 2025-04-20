package integration_test.finance.budget;

import anatolii.k.hoa.HoaApplication;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.application.*;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetCategory;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetException;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlan;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = HoaApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class BudgetPlanEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper json;

    @Autowired
    CreateOrUpdateBudgetPlanUseCase createOrUpdateBudgetPlanUseCase;

    @Autowired
    ChangeBudgetStateUseCases changeBudgetStateUseCases;

    @Autowired
    BudgetPlanRepository budgetPlanRepository;

    @Test
    @Transactional
    void whenCreateNewBudgetPlan_thenOk() throws Exception {

        Year year = Year.now();
        BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                List.of(
                        new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()),
                        new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString())
                ),
                BudgetPlan.Status.DRAFT.toString()
        );

        MvcResult response = mockMvc.perform( put("/api/budget-plan")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content( json.writeValueAsString(budget) )
                ).andExpect( status().isCreated() )
                .andReturn();

        String budgetLocation = response.getResponse().getHeader("Location");
        assertThat(budgetLocation).isNotBlank();

        response = mockMvc.perform( get("/api/budget-plan/{year}", year.getValue())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BudgetPlanDTO receivedBudget = json.readValue(response.getResponse().getContentAsString(), BudgetPlanDTO.class);

        assertThat(receivedBudget).isNotNull();
        assertThat(receivedBudget.getYear()).isEqualTo(budget.getYear());
        assertThat(receivedBudget.getStatus()).isEqualTo(budget.getStatus());
        assertThat(receivedBudget.getCategories().size()).isEqualTo(budget.getCategories().size());
    }

    @Test
    @Transactional
    void givenDraftBudgetPlan_whenUpdate_thenOk() throws Exception {
        Year year = Year.now();
        ArrayList<BudgetCategoryDTO> categories = new ArrayList<>();
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()));
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString()));
        BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                categories,
                BudgetPlan.Status.DRAFT.toString()
        );

        var useCaseResponse = createOrUpdateBudgetPlanUseCase.createOrUpdate(budget);
        assertThat(useCaseResponse.ok()).isTrue();

        {
            Optional<BudgetPlan> budgetPlanDB = budgetPlanRepository.getBudgetPlanForYear(year);
            assertThat(budgetPlanDB.isPresent()).isTrue();
            assertThat(budgetPlanDB.get().getCategories().size()).isEqualTo(2);
        }

        budget.getCategories().removeFirst();

        MvcResult response = mockMvc.perform( put("/api/budget-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( json.writeValueAsString(budget) )
                ).andExpect( status().isNoContent() )
                .andReturn();
        {
            Optional<BudgetPlan> budgetPlanDB = budgetPlanRepository.getBudgetPlanForYear(year);
            assertThat(budgetPlanDB.isPresent()).isTrue();
            assertThat(budgetPlanDB.get().getCategories().size()).isEqualTo(1);
        }
    }

    @Test
    @Transactional
    void givenFinalizedBudgetPlan_whenUpdate_thenFails() throws Exception {
        Year year = Year.now();
        ArrayList<BudgetCategoryDTO> categories = new ArrayList<>();
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()));
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString()));
        BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                categories,
                BudgetPlan.Status.DRAFT.toString()
        );

        var useCaseResponse = createOrUpdateBudgetPlanUseCase.createOrUpdate(budget);
        assertThat(useCaseResponse.ok()).isTrue();

        changeBudgetStateUseCases.changeState(year.getValue(), ChangeBudgetStateUseCases.Action.submit.toString());

        {
            Optional<BudgetPlan> budgetPlanDB = budgetPlanRepository.getBudgetPlanForYear(year);
            assertThat(budgetPlanDB.isPresent()).isTrue();
            assertThat(budgetPlanDB.get().getCategories().size()).isEqualTo(2);
            assertThat(budgetPlanDB.get().isFinalized()).isTrue();
        }

        budget.getCategories().removeFirst();

        MvcResult ret = mockMvc.perform( put("/api/budget-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( json.writeValueAsString(budget) )
                ).andExpect( status().isUnprocessableEntity() )
                .andReturn();

        var response = json.readValue(ret.getResponse().getContentAsString(), UseCaseResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(BudgetException.ErrorCode.BUDGET_ALREADY_FINALIZED.toString());
    }

    @Test
    @Transactional
    void givenDraftBudgetPlan_whenSubmit_thenOk() throws Exception {
        Year year = Year.now();
        ArrayList<BudgetCategoryDTO> categories = new ArrayList<>();
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()));
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString()));
        BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                categories,
                BudgetPlan.Status.DRAFT.toString()
        );

        var useCaseResponse = createOrUpdateBudgetPlanUseCase.createOrUpdate(budget);
        assertThat(useCaseResponse.ok()).isTrue();

        mockMvc.perform( post("/api/budget-plan/{year}/submit", year.getValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( json.writeValueAsString(budget) )
                ).andExpect( status().isOk() );

        {
            Optional<BudgetPlan> budgetPlanDB = budgetPlanRepository.getBudgetPlanForYear(year);
            assertThat(budgetPlanDB.isPresent()).isTrue();
            assertThat(budgetPlanDB.get().isSubmitted()).isTrue();
        }
    }

    @Test
    @Transactional
    void givenDraftBudgetPlan_whenApprove_thenFails() throws Exception {
        Year year = Year.now();
        ArrayList<BudgetCategoryDTO> categories = new ArrayList<>();
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()));
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString()));
        BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                categories,
                BudgetPlan.Status.DRAFT.toString()
        );

        var useCaseResponse = createOrUpdateBudgetPlanUseCase.createOrUpdate(budget);
        assertThat(useCaseResponse.ok()).isTrue();

        var ret = mockMvc.perform( post("/api/budget-plan/{year}/approve", year.getValue())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content( json.writeValueAsString(budget) )
                ).andExpect( status().isUnprocessableEntity() )
                .andReturn();

        var response = json.readValue(ret.getResponse().getContentAsString(), UseCaseResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.ok()).isFalse();
        assertThat(response.errorCode()).isEqualTo(BudgetException.ErrorCode.INCORRECT_STATUS.toString());
    }


}
