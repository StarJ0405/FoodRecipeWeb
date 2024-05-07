package com.StarJ.food_recipe.Entities.Units;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer>, UnitRepositoryCustom {
    @Query("select "
            + "distinct u "
            + "from Unit u "
            + "where "
            + "   u.name is not null "
            + "and "
            + "   u.name = :name ")
    Optional<Unit> findById(String name);
}
