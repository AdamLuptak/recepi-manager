package eu.rebase.recipe.service;

import eu.rebase.recipe.exception.RecipeNotFoundException;
import eu.rebase.recipe.mapper.RecipeMapper;
import eu.rebase.recipe.model.CreateOrUpdateRecipe;
import eu.rebase.recipe.model.Identity;
import eu.rebase.recipe.model.RecipeDetail;
import eu.rebase.recipe.model.RecipeSearchCriteria;
import eu.rebase.recipe.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Service for handling crud operations over recipe aggregate
 */
@Service
@AllArgsConstructor
public class RecipeService {

    public static final String CAN_T_FIND_RECIPE_WITH_ID_TEMPLATE = "Can't find recipe with id: %s";
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeDetail save(CreateOrUpdateRecipe createOrUpdateRecipe, Identity identity) {
        var recipe = recipeMapper.mapRecipe(createOrUpdateRecipe, identity.getId());
        return recipeMapper.mapRecipeDetail(recipeRepository.save(recipe));
    }

    @Transactional
    public RecipeDetail update(UUID id, CreateOrUpdateRecipe createOrUpdateRecipe, Identity identity) {
        var newRecipe = recipeMapper.mapRecipe(createOrUpdateRecipe, identity.getId(), id);
        return recipeRepository.findByIdAndAccountId(id, identity.getId())
                .map(r -> recipeMapper.mapRecipeDetail(recipeRepository.save(newRecipe)))
                .orElseThrow(() -> new RecipeNotFoundException(String.format(CAN_T_FIND_RECIPE_WITH_ID_TEMPLATE, id)));
    }

    public void delete(UUID id, Identity identity) {
        recipeRepository.findByIdAndAccountId(id, identity.getId()).ifPresentOrElse(
                r -> recipeRepository.deleteById(r.getId()), () -> {
                    throw new RecipeNotFoundException(String.format(CAN_T_FIND_RECIPE_WITH_ID_TEMPLATE, id));
                });
    }

    public RecipeDetail get(UUID id, Identity identity) {
        return recipeRepository.findByIdAndAccountId(id, identity.getId())
                .map(recipeMapper::mapRecipeDetail)
                .orElseThrow(() -> new RecipeNotFoundException(String.format(CAN_T_FIND_RECIPE_WITH_ID_TEMPLATE, id)));
    }

    @Transactional
    public Page<RecipeDetail> getAll(RecipeSearchCriteria searchCriteria, Identity identity, Pageable pageable) {
        return recipeRepository.findAll(searchCriteria, identity, pageable).map(recipeMapper::mapRecipeDetail);
    }
}
