package eu.rebase.recipe.boostrap;

import eu.rebase.recipe.entity.EaterType;
import eu.rebase.recipe.entity.Ingredient;
import eu.rebase.recipe.entity.Recipe;
import eu.rebase.recipe.entity.Unit;
import eu.rebase.recipe.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
@Profile("dataset-boostrap")
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Recipe recipe = createRecipe("eggs", "water", "Pizza", "Create pizza", "1", 5, EaterType.VEGETARIAN);
        Recipe recipe2 = createRecipe("bread", "flour", "Bread", "Create Bread", "1", 3, EaterType.VEGETARIAN);

        recipeRepository.saveAll(List.of(recipe,recipe2));
    }

    private static Recipe createRecipe(String firstIngName, String secondIngName, String titiel, String instructions, String accountId, int servingsCount, EaterType vegetarian) {
        var recipe = new Recipe();
        var firstIngredient = new Ingredient();
        firstIngredient.setDescription(firstIngName);
        firstIngredient.setAmount(BigDecimal.valueOf(2));
        firstIngredient.setUnit(new Unit("pcs"));

        var secondIngredient = new Ingredient();
        secondIngredient.setDescription(secondIngName);
        secondIngredient.setAmount(BigDecimal.valueOf(2));
        secondIngredient.setUnit(new Unit("liter"));

        recipe.setTitle(titiel);
        recipe.setInstructions(instructions);
        recipe.setAccountId(accountId);
        recipe.setEaterType(vegetarian);
        recipe.setServingsCount(servingsCount);
        recipe.setIngredients(Set.of(firstIngredient, secondIngredient));

        firstIngredient.setRecipe(recipe);
        secondIngredient.setRecipe(recipe);
        return recipe;
    }
}
