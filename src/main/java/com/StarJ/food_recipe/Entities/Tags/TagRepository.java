package com.StarJ.food_recipe.Entities.Tags;

import com.StarJ.food_recipe.Entities.Tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer>,TagRepositoryCustom {
    @Query("select "
            + "distinct t "
            + "from Tag t "
            + "where "
            + "   t.name is not null "
            + "and "
            + "   t.name = :name ")
    Optional<Tag> findById(String name);

    @Query("select "
            + "distinct t "
            + "from Tag t "
            + "where "
            + "   t.name is not null "
            + "and "
            + "   t.name in :names ")
    List<Tag> findByNames(List<String> names);
}
