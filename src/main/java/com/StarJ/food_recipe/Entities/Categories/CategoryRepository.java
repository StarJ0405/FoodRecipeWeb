package com.StarJ.food_recipe.Entities.Categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("select "
            + "distinct c "
            + "from Category c "
            + "where "
            + "   c.name is not null "
            + "and "
            + "   c.name = :name ")
    Optional<Category> findById(String name);
}
