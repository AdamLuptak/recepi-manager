package eu.rebase.recipe.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class Unit implements Serializable {
    @NotBlank
    private String value;

    public Unit() {
    }

    public Unit(String value) {
        this.value = value;
    }
}
