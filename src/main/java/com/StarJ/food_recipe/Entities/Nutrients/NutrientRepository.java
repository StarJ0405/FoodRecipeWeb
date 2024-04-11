package com.StarJ.food_recipe.Entities.Nutrients;

import com.StarJ.food_recipe.Entities.Units.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Integer> {
    @Query("select "
            + "distinct n "
            + "from Nutrient n "
            + "where "
            + "   n.name is not null "
            + "and "
            + "   n.name = :name ")
    Optional<Nutrient> findById(String name);

}
