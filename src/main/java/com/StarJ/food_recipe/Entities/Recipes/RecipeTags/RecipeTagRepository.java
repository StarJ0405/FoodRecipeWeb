package com.StarJ.food_recipe.Entities.Recipes.RecipeTags;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RecipeTagRepository extends JpaRepository<RecipeTag, Integer> {
    @Query("select "
            + "distinct rt "
            + "from RecipeTag rt "
            + "left outer join Recipe r on rt.recipe =r "
            + "left outer join Tag t on rt.tag =t "
            + "where "
            + "r.id = :recipe_id "
            + "and "
            + "t.name = :tag_name "
    )
    Optional<RecipeTag> find(int recipe_id, String tag_name);
}
