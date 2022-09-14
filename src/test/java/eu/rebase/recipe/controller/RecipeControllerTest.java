package eu.rebase.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.rebase.recipe.model.*;
import eu.rebase.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static eu.rebase.recipe.controller.IdentityArgumentResolver.X_USERID;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateRecipe() throws Exception {
        // given
        var recipeId = UUID.randomUUID();
        var userId = UUID.randomUUID().toString();

        var firstIngredient = createIngredient();
        var ingredientsSet = Set.of(firstIngredient);
        var createRecipe = createRecipe(ingredientsSet);
        var expectedRecipe = createRecipeDetail(recipeId, ingredientsSet, createRecipe);

        given(recipeService.save(any(), any())).willReturn(expectedRecipe);

        // when
        var resultActions = mvc.perform(post("/api/v1/recipes")
                .content(objectMapper.writeValueAsBytes(createRecipe))
                .header(X_USERID, userId)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        assertRecipe(resultActions, status().isCreated(), expectedRecipe, firstIngredient);
    }

    @Test
    void shouldGetRecipeById() throws Exception {
        // given
        var recipeId = UUID.randomUUID();
        var userId = UUID.randomUUID().toString();

        var firstIngredient = createIngredient();
        var ingredientsSet = Set.of(firstIngredient);
        var createRecipe = createRecipe(ingredientsSet);
        var expectedRecipe = createRecipeDetail(recipeId, ingredientsSet, createRecipe);

        given(recipeService.get(any(), any())).willReturn(expectedRecipe);

        // when
        var resultActions = mvc.perform(get("/api/v1/recipes/" + recipeId)
                .header(X_USERID, userId)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        assertRecipe(resultActions, status().isOk(), expectedRecipe, firstIngredient);
    }

    @Test
    void shouldDeleteRecipeById() throws Exception {
        // given
        var recipeId = UUID.randomUUID().toString();
        var userId = UUID.randomUUID().toString();

        Ingredient firstIngredient = createIngredient();
        var ingredientsSet = Set.of(firstIngredient);

        willDoNothing().given(recipeService).delete(any(), any());

        // when
        var resultActions = mvc.perform(delete("/api/v1/recipes/" + recipeId)
                .header(X_USERID, userId)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateRecipeById() throws Exception {
        // given
        var recipeId = UUID.randomUUID();
        var userId = UUID.randomUUID().toString();

        var firstIngredient = createIngredient();
        var ingredientsSet = Set.of(firstIngredient);
        var createRecipe = createRecipe(ingredientsSet);
        var expectedRecipe = createRecipeDetail(recipeId, ingredientsSet, createRecipe);

        given(recipeService.update(any(), any(), any())).willReturn(expectedRecipe);

        // when
        var resultActions = mvc.perform(put("/api/v1/recipes/" + recipeId)
                .content(objectMapper.writeValueAsBytes(createRecipe))
                .header(X_USERID, userId)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        assertRecipe(resultActions, status().isOk(), expectedRecipe, firstIngredient);
    }

    private static RecipeDetail createRecipeDetail(UUID recipeId, Set<Ingredient> ingredientsSet, CreateOrUpdateRecipe createRecipe) {
        return RecipeDetail.builder()
                .id(recipeId)
                .title(createRecipe.getTitle())
                .instructions(createRecipe.getInstructions())
                .ingredients(ingredientsSet)
                .servingsCount(2)
                .eaterType(EaterType.VEGETARIAN)
                .build();
    }

    private static Ingredient createIngredient() {
        return Ingredient.builder()
                .unit(Unit.builder()
                        .description("pcs")
                        .build())
                .description("eggs")
                .amount(BigDecimal.valueOf(2))
                .build();
    }

    private static CreateOrUpdateRecipe createRecipe(Set<Ingredient> ingredientsSet) {
        return CreateOrUpdateRecipe.builder()
                .title("Crispy Salt and Pepper Potatoes")
                .instructions("Instructions")
                .ingredients(ingredientsSet)
                .servingsCount(2)
                .eaterType(EaterType.VEGETARIAN)
                .build();
    }

    private static void assertRecipe(ResultActions resultActions, ResultMatcher Created, RecipeDetail recipeDetail, Ingredient firstIngredient) throws Exception {
        resultActions.andExpect(Created)
                .andExpect(jsonPath("$.title", is(recipeDetail.getTitle())))
                .andExpect(jsonPath("$.instructions", is(recipeDetail.getInstructions())))
                .andExpect(jsonPath("$.ingredients.length()", is(1)))
                .andExpect(jsonPath("$.ingredients[0].description", is(firstIngredient.getDescription())))
                .andExpect(jsonPath("$.ingredients[0].amount", is(firstIngredient.getAmount().intValueExact())))
                .andExpect(jsonPath("$.ingredients[0].unit.description", is(firstIngredient.getUnit().getDescription())));
    }

}