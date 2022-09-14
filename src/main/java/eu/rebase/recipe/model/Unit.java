package eu.rebase.recipe.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder(toBuilder = true)
public class Unit {
    @NotBlank
    String description;
}
