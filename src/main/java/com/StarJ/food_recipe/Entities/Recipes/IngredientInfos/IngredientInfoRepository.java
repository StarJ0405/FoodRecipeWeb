package com.StarJ.food_recipe.Entities.Recipes.IngredientInfos;

import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IngredientInfoRepository extends JpaRepository<IngredientInfo, Integer> {
    @Query("select "
            + "distinct ii "
            + "from IngredientInfo ii "
            + "left outer join Ingredient i on ii.ingredient=i "
            + "left outer join Recipe r on ii.recipe=r "
            + "where "
            + "r.id = :recipe_id "
            + "and "
            + "i.name = :ingredient_name "
    )
    Optional<IngredientInfo> find(String ingredient_name, int recipe_id);

}
