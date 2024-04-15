package com.StarJ.food_recipe.Entities.Tools;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ToolRepository extends JpaRepository<Tool, Integer> {
    @Query("select "
            + "distinct t "
            + "from Tool t "
            + "where "
            + "   t.name is not null "
            + "and "
            + "   t.name = :name ")
    Optional<Tool> findById(String name);

    @Query("select "
            + "distinct t "
            + "from Tool t "
            + "where "
            + "   t.name is not null "
            + "and "
            + "   t.name in :names ")
    List<Tool> findByNames(List<String> names);

}
