package com.StarJ.food_recipe.Entities.Recipes.BodyImages;

import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BodyImageRepository extends JpaRepository<BodyImage, Integer> {
    @Query("select "
            + "distinct bi "
            + "from BodyImage bi "
            + "left outer join Recipe r on bi.recipe=r "
            + "where "
            + "bi.id = :recipe_id "
            + "and "
            + "bi.body = :body "
            + "and "
            + "bi.imgURL = :imgURL "
    )
    Optional<BodyImage> find(int recipe_id, String body, String imgURL);
}
