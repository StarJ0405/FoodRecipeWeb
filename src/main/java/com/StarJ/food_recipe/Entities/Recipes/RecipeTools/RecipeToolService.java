package com.StarJ.food_recipe.Entities.Recipes.RecipeTools;

import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeToolService {
    private final RecipeToolRepository recipeToolRepository;

    public RecipeTool getRecipeTool(Recipe recipe, Tool tool) {
        Optional<RecipeTool> _recipeTool = recipeToolRepository.find(recipe.getId(), tool.getName());
        if (_recipeTool.isPresent())
            return _recipeTool.get();
        else
            return recipeToolRepository.save(RecipeTool.builder().recipe(recipe).tool(tool).build());
    }

    public void reset() {
        recipeToolRepository.deleteAll();
    }

}
