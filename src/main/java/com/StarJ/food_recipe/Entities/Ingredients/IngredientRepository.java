package com.StarJ.food_recipe.Entities.Ingredients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer>,IngredientRepositoryCustom {

    @Query("select "
            + "distinct i "
            + "from Ingredient i "
            + "where "
            + "   i.name is not null "
            + "and "
            + "   i.name = :name ")
    Optional<Ingredient> findById(String name);
}
