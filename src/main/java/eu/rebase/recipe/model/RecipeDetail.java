package eu.rebase.recipe.model;

import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class RecipeDetail {
    UUID id;
    String title;
    String instructions;
    Set<Ingredient> ingredients;
    EaterType eaterType;
    Integer servingsCount;
    Long version;
}
