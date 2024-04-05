package com.StarJ.food_recipe.Entities.Tools;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ToolRepository extends JpaRepository<Tool, Integer> {
    @Query("select "
            + "distinct t "
            + "from Tool t "
            + "where "
            + "   t.name is not null "
            + "and "
            + "   t.name = :name ")
    Optional<Tool> findById(String name);
}
