package eu.rebase.recipe.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eu.rebase.recipe.entity.Recipe;
import eu.rebase.recipe.model.Identity;
import eu.rebase.recipe.model.RecipeSearchCriteria;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static eu.rebase.recipe.entity.QIngredient.ingredient;
import static eu.rebase.recipe.entity.QRecipe.recipe;

/**
 * Specific impl of repository used for advance search capabilities
 */
public class CustomRecipeRepositoryImpl implements CustomRecipeRepository {

    private final JPAQueryFactory queryFactory;
    private final RecipeRepository recipeRepository;
    private final RecipePredicateExtractor recipePredicateExtractor;

    public CustomRecipeRepositoryImpl(JPAQueryFactory queryFactory,
                                      @Lazy RecipeRepository recipeRepository,
                                      RecipePredicateExtractor recipePredicateExtractor) {
        this.queryFactory = queryFactory;
        this.recipeRepository = recipeRepository;
        this.recipePredicateExtractor = recipePredicateExtractor;
    }

    @Override
    public Page<Recipe> findAll(RecipeSearchCriteria searchCriteria, Identity identity, Pageable pageable) {
        var mainQuery = queryFactory
                .select(recipe)
                .from(recipe)
                .where(recipePredicateExtractor.extractMainPredicate(searchCriteria,identity))
                .innerJoin(recipe.ingredients);

        excludeIngredientsQueryConfigure(searchCriteria, mainQuery);
        includeIngredientsQueryConfigure(searchCriteria, mainQuery);

        return PageableExecutionUtils.getPage(mainQuery.distinct().fetch(), pageable, recipeRepository::count);
    }

    private static void includeIngredientsQueryConfigure(RecipeSearchCriteria searchCriteria, JPAQuery<Recipe> mainQuery) {
        searchCriteria.getIncludeIngredients().ifPresent(sc -> {
            var includeQuery = JPAExpressions.select(ingredient.recipe.id)
                    .from(ingredient)
                    .where(ingredient.description.in(sc));

            mainQuery.where(recipe.id.in(includeQuery));
        });
    }

    private static void excludeIngredientsQueryConfigure(RecipeSearchCriteria searchCriteria, JPAQuery<Recipe> mainQuery) {
        searchCriteria.getExcludeIngredients().ifPresent(sc -> {
            var excludeQuery = JPAExpressions.select(ingredient.recipe.id)
                    .from(ingredient)
                    .where(ingredient.description.in(sc));
            mainQuery.where(recipe.id.notIn(excludeQuery));
        });
    }
}
