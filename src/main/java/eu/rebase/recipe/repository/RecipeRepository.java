package eu.rebase.recipe.repository;

import eu.rebase.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID>, QuerydslPredicateExecutor<Recipe>, CustomRecipeRepository {

    Optional<Recipe> findByIdAndAccountId(UUID id, String accountId);
}
