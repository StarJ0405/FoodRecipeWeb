package com.StarJ.food_recipe.Entities.Recipes.RecipeEvals;

import com.StarJ.food_recipe.Entities.Configs.Config;
import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Recipes.RecipeService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeEvalService {
    private final RecipeEvalRepository recipeEvalRepository;
    private final RecipeService recipeService;

    public void reset() {
        recipeEvalRepository.deleteAll();
    }

    public RecipeEval setEval(SiteUser user, Recipe recipe, double val) {
        Optional<RecipeEval> _recipeEval = recipeEvalRepository.findByUserRecipe(user, recipe);
        if (_recipeEval.isPresent())
            recipeService.removeRecipeEval(_recipeEval.get());
        RecipeEval recipeEval = RecipeEval.builder().recipe(recipe).siteUser(user).build();
        recipeEval.setVal(val);
        recipeEvalRepository.save(recipeEval);
        return recipeEval;
    }

    public RecipeEval getEval(SiteUser user, Recipe recipe) {
        Optional<RecipeEval> _recipeEval = recipeEvalRepository.findByUserRecipe(user, recipe);
        return _recipeEval.isPresent() ? _recipeEval.get() : null;
    }

    public List<RecipeEval> getEvalsByLimited(Integer start) {
        return recipeEvalRepository.findAfterIdByLimited(start);
    }
    public List<RecipeEval> getEvalsByLimited() {
        return recipeEvalRepository.findAllByLimited();
    }

    public Integer getLastEvalID(Config config) {
        Integer start = config.getIntegerValue();
        if(start == null)
            start =0;
        Integer last = recipeEvalRepository.getLastRecipeID();
        return last > start + 1000 ? (start + 1000) : last;
    }


    public List<RecipeEval> getEvals() {
        return recipeEvalRepository.findAll();
    }

    public Long getCount() {
        return recipeEvalRepository.getCount();
    }

}
