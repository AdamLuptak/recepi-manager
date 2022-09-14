package eu.rebase.recipe.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
@Builder(toBuilder = true)
public class Ingredient {
    @NotBlank
    String description;
    @NotNull
    BigDecimal amount;
    @NotNull
    @Valid
    Unit unit;
}
