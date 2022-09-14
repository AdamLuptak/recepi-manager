package eu.rebase.recipe.controller;

import eu.rebase.recipe.model.*;
import eu.rebase.recipe.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

/**
 * Controller used to perform crud operatios
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/recipes")
@Validated
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDetail createRecipe(@Valid @RequestBody CreateOrUpdateRecipe createOrUpdateRecipe, Identity identity) {
        return recipeService.save(createOrUpdateRecipe, identity);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeDetail updateRecipe(@PathVariable(value = "id") UUID id, @RequestBody CreateOrUpdateRecipe createOrUpdateRecipe, Identity identity) {
        return recipeService.update(id, createOrUpdateRecipe, identity);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable(value = "id") UUID id, Identity identity) {
        recipeService.delete(id, identity);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeDetail getRecipe(@PathVariable(value = "id") UUID id, Identity identity) {
        return recipeService.get(id, identity);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<RecipeDetail> getAll(@RequestParam(required = false) EaterType eaterType,
                                     @RequestParam(required = false) String title,
                                     @RequestParam(required = false) Integer servingsCount,
                                     @RequestParam(required = false) Set<String> includeIngredients,
                                     @RequestParam(required = false) Set<String> excludeIngredients,
                                     @RequestParam(required = false) String instructions,
                                     Identity identity,
                                     Pageable pageable) {
        var searchCriteria = RecipeSearchCriteria.builder()
                .eaterType(eaterType)
                .title(title)
                .servingsCount(servingsCount)
                .includeIngredients(includeIngredients)
                .excludeIngredients(excludeIngredients)
                .instructions(instructions)
                .build();

        return recipeService.getAll(searchCriteria, identity, pageable);
    }
}
