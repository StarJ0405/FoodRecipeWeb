package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Users.SiteUser;

import java.util.List;

public interface RecipeRepositoryCustom {
    List<Recipe> search(SiteUser author);
}
