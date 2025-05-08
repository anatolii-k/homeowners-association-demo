package integration_test.finance.budget;

import anatolii.k.hoa.HoaApplication;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.finance.budget.internal.application.*;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetCategory;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetException;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlan;
import anatolii.k.hoa.finance.budget.internal.domain.BudgetPlanRepository;
import anatolii.k.hoa.security.Roles;
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

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    CreateBudgetPlanUseCase createBudgetPlanUseCase;

    @Autowired
    BudgetPlanRepository budgetPlanRepository;

    @Test
    @Transactional
    @WithMockUser(roles = Roles.BOARD_MEMBER)
    void whenCreateNewBudgetPlan_thenOk() throws Exception {

        Year year = Year.now();
        BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                List.of(
                        new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()),
                        new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString())
                ),
                BudgetPlan.Status.DRAFT.toString()
        );

        MvcResult response = mockMvc.perform( post("/api/budget-plan")
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
    @WithMockUser(roles = Roles.BOARD_MEMBER)
    void givenDraftBudgetPlan_whenSubmit_thenOk() throws Exception {
        Year year = Year.now();
        ArrayList<BudgetCategoryDTO> categories = new ArrayList<>();
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()));
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString()));
        BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                categories,
                BudgetPlan.Status.DRAFT.toString()
        );

        var useCaseResponse = createBudgetPlanUseCase.create(budget);
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
    @WithMockUser(roles = Roles.BOARD_MEMBER)
    void givenDraftBudgetPlan_whenApprove_thenFails() throws Exception {
        Year year = Year.now();
        ArrayList<BudgetCategoryDTO> categories = new ArrayList<>();
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()));
        categories.add(new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString()));
        BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                categories,
                BudgetPlan.Status.DRAFT.toString()
        );

        var useCaseResponse = createBudgetPlanUseCase.create(budget);
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

    @Test
    @Transactional
    @WithMockUser(roles = Roles.BOARD_MEMBER)
    void givenDraftBudgetPlan_whenUpdateCategory_thenOk() throws Exception {
        Year year = Year.now();
        {
            // create budget plan

            ArrayList<BudgetCategoryDTO> categories = new ArrayList<>();
            categories.add(new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()));
            categories.add(new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString()));
            BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                    categories,
                    BudgetPlan.Status.DRAFT.toString()
            );
            var useCaseResponse = createBudgetPlanUseCase.create(budget);
            assertThat(useCaseResponse.ok()).isTrue();
        }
        String categoryNewName = "updated category";
        Long categoryId;
        {
            // update first category

            BudgetPlan budget = budgetPlanRepository.getBudgetPlanForYear(year)
                    .orElseThrow();
            BudgetCategory category = budget.getCategories().getFirst();
            categoryId = category.getId();
            category.setName(categoryNewName);


            mockMvc.perform( put("/api/budget-plan/{year}/category", year.getValue())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content( json.writeValueAsString(BudgetCategoryDTO.fromDomain(category)) )
                    ).andExpect( status().isNoContent() );
        }
        {
            //check that category is updated in DB

            BudgetPlan budgetDB = budgetPlanRepository.getBudgetPlanForYear(year)
                    .orElseThrow();
            BudgetCategory updatedCategory = budgetDB.getCategories()
                    .stream()
                    .filter(category -> category.getId().equals(categoryId))
                    .findAny()
                    .orElseThrow();

            assertThat(updatedCategory.getName()).isEqualTo(categoryNewName);
        }
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.BOARD_MEMBER)
    void givenDraftBudgetPlan_whenAddNewCategory_thenOk() throws Exception {
        Year year = Year.now();
        {
            // create budget plan

            ArrayList<BudgetCategoryDTO> categories = new ArrayList<>();
            categories.add(new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()));
            categories.add(new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString()));
            BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                    categories,
                    BudgetPlan.Status.DRAFT.toString()
            );
            var useCaseResponse = createBudgetPlanUseCase.create(budget);
            assertThat(useCaseResponse.ok()).isTrue();
        }
        String newCategoryName = "new category";
        Set<Long> existingCategoriesIds;
        {
            // add new category

            BudgetPlan budget = budgetPlanRepository.getBudgetPlanForYear(year)
                    .orElseThrow();
            existingCategoriesIds = budget.getCategories()
                            .stream()
                            .map( BudgetCategory::getId )
                            .collect(Collectors.toSet());

            var newCategory = new BudgetCategoryDTO(year.getValue(), null, newCategoryName, "", BigDecimal.ONE, BudgetCategory.AllocationType.PER_UNIT.toString());

            mockMvc.perform( put("/api/budget-plan/{year}/category", year.getValue())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content( json.writeValueAsString(newCategory) )
            ).andExpect( status().isNoContent() );
        }
        {
            //check that category is updated in DB

            BudgetPlan budgetDB = budgetPlanRepository.getBudgetPlanForYear(year)
                    .orElseThrow();
            assertThat(budgetDB.getCategories().size()).isEqualTo( existingCategoriesIds.size() + 1 );

            BudgetCategory updatedCategory = budgetDB.getCategories()
                    .stream()
                    .filter(category -> !existingCategoriesIds.contains(category.getId()))
                    .findAny()
                    .orElseThrow();

            assertThat(updatedCategory.getName()).isEqualTo(newCategoryName);
        }
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.BOARD_MEMBER)
    void givenDraftBudgetPlan_whenDeleteCategory_thenOk() throws Exception {
        Year year = Year.now();
        {
            // create budget plan

            ArrayList<BudgetCategoryDTO> categories = new ArrayList<>();
            categories.add(new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()));
            categories.add(new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString()));
            BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                    categories,
                    BudgetPlan.Status.DRAFT.toString()
            );
            var useCaseResponse = createBudgetPlanUseCase.create(budget);
            assertThat(useCaseResponse.ok()).isTrue();
        }
        Long categoryId;
        {
            // delete first category

            BudgetPlan budget = budgetPlanRepository.getBudgetPlanForYear(year)
                    .orElseThrow();
            BudgetCategory category = budget.getCategories().getFirst();
            categoryId = category.getId();


            mockMvc.perform( delete("/api/budget-plan/{year}/category/{id}", year.getValue(), categoryId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content( json.writeValueAsString(BudgetCategoryDTO.fromDomain(category)) )
            ).andExpect( status().isNoContent() );
        }
        {
            //check that category is deleted from DB

            BudgetPlan budgetDB = budgetPlanRepository.getBudgetPlanForYear(year)
                    .orElseThrow();

            var deletedCategory = budgetDB.getCategories()
                    .stream()
                    .filter(category -> category.getId().equals(categoryId))
                    .findAny();

            assertThat(deletedCategory.isEmpty()).isTrue();
        }
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.USER)
    void givenUserWithUserRole_whenCreateNewBudgetPlan_thenNotAllowed() throws Exception {

        Year year = Year.now();
        BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                List.of(
                        new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()),
                        new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString())
                ),
                BudgetPlan.Status.DRAFT.toString()
        );

        mockMvc.perform( post("/api/budget-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( json.writeValueAsString(budget) )
                ).andExpect( status().isForbidden() );
    }

    @Test
    @Transactional
    @WithMockUser(roles = Roles.ADMIN)
    void givenUserWithAdminRole_whenCreateNewBudgetPlan_thenNotAllowed() throws Exception {

        Year year = Year.now();
        BudgetPlanDTO budget = new BudgetPlanDTO( year.getValue(),
                List.of(
                        new BudgetCategoryDTO(year.getValue(), null, "one", "", BigDecimal.TEN, BudgetCategory.AllocationType.PER_UNIT.toString()),
                        new BudgetCategoryDTO(year.getValue(), null, "two", "", BigDecimal.TWO, BudgetCategory.AllocationType.PER_UNIT.toString())
                ),
                BudgetPlan.Status.DRAFT.toString()
        );

        mockMvc.perform( post("/api/budget-plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content( json.writeValueAsString(budget) )
        ).andExpect( status().isForbidden() );
    }

}
