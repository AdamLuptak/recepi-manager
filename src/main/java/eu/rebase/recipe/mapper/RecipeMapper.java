package eu.rebase.recipe.mapper;

import eu.rebase.recipe.entity.Recipe;
import eu.rebase.recipe.model.CreateOrUpdateRecipe;
import eu.rebase.recipe.model.Ingredient;
import eu.rebase.recipe.model.RecipeDetail;
import eu.rebase.recipe.model.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    @Mapping(source = "accountId", target = "accountId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    Recipe mapRecipe(CreateOrUpdateRecipe createOrUpdateRecipe, String accountId);

    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "id", target = "id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Recipe mapRecipe(CreateOrUpdateRecipe createOrUpdateRecipe, String accountId, UUID id);

    @Mapping(source = "value", target = "description")
    Unit mapUnit(eu.rebase.recipe.entity.Unit unit);

    @Mapping(source = "description", target = "value")
    eu.rebase.recipe.entity.Unit mapUnit(Unit unit);

    Set<Ingredient> mapIngredients(Set<eu.rebase.recipe.entity.Ingredient> ingredients);

    RecipeDetail mapRecipeDetail(Recipe recipe);
}
