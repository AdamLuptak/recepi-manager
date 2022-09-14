package eu.rebase.recipe.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class CreateOrUpdateRecipe {
    @NotBlank
    @Size(max = 1000)
    String title;
    @NotBlank
    @Size(max = 10000)
    String instructions;
    @NotNull
    Set<@Valid Ingredient> ingredients;
    @NotNull EaterType eaterType;
    @NotNull
    Integer servingsCount;
    Long version;
}
