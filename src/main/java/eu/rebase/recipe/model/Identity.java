package eu.rebase.recipe.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Identity {
    String id;
}
