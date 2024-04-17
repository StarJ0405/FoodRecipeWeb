package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import org.apache.poi.sl.draw.geom.GuideIf;

import java.util.List;
import java.util.Optional;

public interface RecipeRepositoryCustom {
    List<Recipe> search(SiteUser author);

    Optional<Recipe> search(String subject, List<Ingredient> ingredients);
    Optional<Recipe> search(String subject);
}
