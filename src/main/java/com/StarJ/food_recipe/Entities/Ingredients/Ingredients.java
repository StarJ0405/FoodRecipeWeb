package com.StarJ.food_recipe.Entities.Ingredients;

import com.StarJ.food_recipe.Entities.Units.Unit;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Ingredients {
    @Id
    private String name;
    private int cal;
    @Column(columnDefinition = "TEXT")
    private String info;

    @ManyToOne
    private Unit unit;
    @ManyToMany
    private List<Nutrient> nutrients;
}
