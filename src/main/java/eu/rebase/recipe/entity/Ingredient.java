package eu.rebase.recipe.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Ingredient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotBlank
    String description;
    @NotNull
    BigDecimal amount;

    @Embedded
    @NotNull
    private Unit unit;

    @ManyToOne
    private Recipe recipe;

    public void setRecipe(Recipe recipe) {
        this.setAccountId(recipe.getAccountId());
        this.recipe = recipe;
    }
}
