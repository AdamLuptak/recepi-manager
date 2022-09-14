package eu.rebase.recipe.model;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class RecipeSearchCriteria {

    EaterType eaterType;
    String title;
    Integer servingsCount;
    Set<String> includeIngredients;
    Set<String> excludeIngredients;
    String instructions;

    public Optional<EaterType> getEaterType() {
        return Optional.ofNullable(eaterType);
    }

    public Optional<Integer> getServingsCount() {
        return Optional.ofNullable(servingsCount);
    }

    public Optional<String> getInstructions() {
        return Optional.ofNullable(instructions);
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public Optional<Set<String>> getIncludeIngredients() {
        return Optional.ofNullable(includeIngredients);
    }

    public Optional<Set<String>> getExcludeIngredients() {
        return Optional.ofNullable(excludeIngredients);
    }
}
