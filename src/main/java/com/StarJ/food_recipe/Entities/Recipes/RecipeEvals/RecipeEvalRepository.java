package com.StarJ.food_recipe.Entities.Recipes.RecipeEvals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeEvalRepository extends JpaRepository<RecipeEval, Integer>, RecipeEvalCustom {
}
