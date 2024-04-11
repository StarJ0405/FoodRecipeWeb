package com.StarJ.food_recipe.Entities.Recipes.RecipeTags;

import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTools.RecipeTool;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeTagService {
    private final RecipeTagRepository recipeTagRepository;

    public RecipeTag getRecipeTag(Recipe recipe, Tag tag) {
        Optional<RecipeTag> _recipeTag = recipeTagRepository.find(recipe.getId(), tag.getName());
        if (_recipeTag.isPresent())
            return _recipeTag.get();
        else
            return recipeTagRepository.save(RecipeTag.builder().recipe(recipe).tag(tag).build());
    }
}
