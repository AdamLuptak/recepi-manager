package eu.rebase.recipe.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import eu.rebase.recipe.entity.QRecipe;
import eu.rebase.recipe.model.Identity;
import eu.rebase.recipe.model.RecipeSearchCriteria;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecipePredicateExtractorTest {


    @Test
    void shouldExtractJustAccountIdInPredicate() {
        // given
        var extractor = new RecipePredicateExtractor();
        var identity = Identity.builder().id("1").build();
        var searchCriteria = RecipeSearchCriteria.builder().build();
        // when
        BooleanExpression actual = (BooleanExpression) extractor.extractMainPredicate(searchCriteria, identity);
        // then
        assertThat(actual)
                .isNotNull()
                .isEqualTo(QRecipe.recipe.accountId.eq("1"));
    }
}