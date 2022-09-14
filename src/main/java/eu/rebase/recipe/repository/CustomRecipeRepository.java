package eu.rebase.recipe.repository;

import eu.rebase.recipe.entity.Recipe;
import eu.rebase.recipe.model.Identity;
import eu.rebase.recipe.model.RecipeSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomRecipeRepository {

    Page<Recipe> findAll(RecipeSearchCriteria searchCriteria, Identity identity, Pageable pageable);
}
