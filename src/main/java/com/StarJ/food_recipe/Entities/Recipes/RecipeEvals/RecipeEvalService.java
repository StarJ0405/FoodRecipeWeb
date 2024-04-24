package com.StarJ.food_recipe.Entities.Recipes.RecipeEvals;

import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeEvalService {
    private final RecipeEvalRepository recipeEvalRepository;

    public RecipeEval setEval(SiteUser user, Recipe recipe, double val) {
        Optional<RecipeEval> _recipeEval = recipeEvalRepository.findByUserRecipe(user, recipe);
        RecipeEval recipeEval = _recipeEval.orElseGet(() -> RecipeEval.builder().recipe(recipe).siteUser(user).build());
        recipeEval.setVal(val);
        recipeEvalRepository.save(recipeEval);
        return recipeEval;
    }

    public RecipeEval getEval(SiteUser user, Recipe recipe) {
        Optional<RecipeEval> _recipeEval = recipeEvalRepository.findByUserRecipe(user, recipe);
        return _recipeEval.isPresent() ? _recipeEval.get() : null;
    }

    public List<RecipeEval> getEvals(Integer start) {
        return recipeEvalRepository.findAfterId(start);
    }

    public Integer getLastEvalID() {
        return recipeEvalRepository.getLastRecipeID();
    }

    public List<RecipeEval> getEvals() {
        return recipeEvalRepository.findAll();
    }
}
