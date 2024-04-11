package com.StarJ.food_recipe.Entities.Recipes.RecipeTools;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RecipeToolRepository extends JpaRepository<RecipeTool, Integer> {
    @Query("select "
            + "distinct rt "
            + "from RecipeTool rt "
            + "left outer join Recipe r on rt.recipe =r "
            + "left outer join Tool t on rt.tool =t "
            + "where "
            + "r.id = :recipe_id "
            + "and "
            + "t.name = :tool_name "
    )
    Optional<RecipeTool> find(int recipe_id, String tool_name);
}
