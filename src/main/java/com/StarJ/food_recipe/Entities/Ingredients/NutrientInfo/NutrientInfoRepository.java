package com.StarJ.food_recipe.Entities.Ingredients.NutrientInfo;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NutrientInfoRepository extends JpaRepository<NutrientInfo, Integer> {
    @Query("select "
            + "distinct ni "
            + "from NutrientInfo ni "
            + "left outer join Nutrient n on ni.nutrient=n "
            + "left outer join Ingredient i on ni.ingredient=i "
            + "where "
            + "n.name = :nutrient_name "
            + "and "
            + "i.name = :ingredient_name "
    )
    Optional<NutrientInfo> find(String ingredient_name, String nutrient_name);
}
