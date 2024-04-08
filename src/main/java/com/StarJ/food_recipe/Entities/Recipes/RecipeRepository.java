package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    @Query("select "
            + "distinct r "
            + "from Recipe r "
            + "where "
            + "   r.id is not null "
            + "and "
            + "   r.id = :name ")
    Optional<Recipe> findById(String name);
}
