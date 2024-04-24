package com.StarJ.food_recipe.Entities.Recipes.RecipeEvals;

import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Recipes.RecipeRepository;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.hibernate.query.sqm.internal.QuerySqmImpl;

import java.util.List;
import java.util.Optional;

public interface RecipeEvalCustom  {
    List<RecipeEval> findAfterId(Integer id);
    List<RecipeEval> findByRecipe(Recipe recipe);
    List<RecipeEval> findByUser(SiteUser user);
    Optional<RecipeEval> findByUserRecipe(SiteUser user, Recipe recipe);
    int getLastRecipeID();
}
