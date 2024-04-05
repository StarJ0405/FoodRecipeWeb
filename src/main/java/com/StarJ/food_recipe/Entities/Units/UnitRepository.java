package com.StarJ.food_recipe.Entities.Units;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
    @Query("select "
            + "distinct u "
            + "from Unit u "
            + "where "
            + "   u.name is not null "
            + "and "
            + "   u.name = :name ")
    Optional<Unit> findById(String name);
}
