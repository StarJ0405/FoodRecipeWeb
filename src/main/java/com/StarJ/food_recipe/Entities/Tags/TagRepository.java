package com.StarJ.food_recipe.Entities.Tags;

import com.StarJ.food_recipe.Entities.Tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("select "
            + "distinct t "
            + "from Tag t "
            + "where "
            + "   t.name is not null "
            + "and "
            + "   t.name = :name ")
    Optional<Tag> findById(String name);
}
