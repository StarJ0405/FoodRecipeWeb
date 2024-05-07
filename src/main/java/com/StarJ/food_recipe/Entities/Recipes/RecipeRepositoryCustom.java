package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecipeRepositoryCustom {
    List<Recipe> search(SiteUser author);

    Optional<Recipe> search(String subject, List<Ingredient> ingredients);

    Optional<Recipe> search(String subject);

    List<Integer> unseenSearch(String user);

    Page<Recipe> recipePage(Pageable pageable);

    Page<Recipe> recipePage(Pageable pageable, String kw);
    Page<Recipe> recipePage(Pageable pageable, String kw, List<String> tags);
    public Long getCount();
}
