package eu.rebase.recipe.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import eu.rebase.recipe.entity.EaterType;
import eu.rebase.recipe.model.Identity;
import eu.rebase.recipe.model.RecipeSearchCriteria;
import org.springframework.stereotype.Component;

import static eu.rebase.recipe.entity.QRecipe.recipe;

@Component
public class RecipePredicateExtractor {

    public Predicate extractMainPredicate(RecipeSearchCriteria searchCriteria, Identity identity) {
        var bb = new BooleanBuilder();
        bb.and(recipe.accountId.eq(identity.getId()));
        searchCriteria.getEaterType().ifPresent(et -> bb.and(recipe.eaterType.eq(EaterType.valueOf(et.name()))));
        searchCriteria.getServingsCount().ifPresent(sc -> bb.and(recipe.servingsCount.eq(sc)));
        searchCriteria.getInstructions().ifPresent(sc -> bb.and(recipe.instructions.containsIgnoreCase(sc)));
        searchCriteria.getTitle().ifPresent(sc -> bb.and(recipe.title.eq(sc)));
        return bb.getValue();
    }
}
