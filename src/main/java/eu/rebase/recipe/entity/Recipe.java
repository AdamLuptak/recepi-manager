package eu.rebase.recipe.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Recipe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    @NotNull
    private Long version;

    @NotBlank
    private String title;
    @NotBlank
    private String instructions;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "recipe", orphanRemoval = true)
    private Set<Ingredient> ingredients = new HashSet<>();
    @NotNull
    private EaterType eaterType;
    @NotNull
    private Integer servingsCount;

    public void setIngredients(Set<Ingredient> ingredients) {
        ingredients.forEach(i -> i.setRecipe(this));
        this.ingredients = ingredients;
    }
}
