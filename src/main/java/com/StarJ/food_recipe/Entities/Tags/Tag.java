package com.StarJ.food_recipe.Entities.Tags;

import com.StarJ.food_recipe.Entities.Categories.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Tag {
    @Id
    private String name;

    @ManyToOne
    private Category category;
}
