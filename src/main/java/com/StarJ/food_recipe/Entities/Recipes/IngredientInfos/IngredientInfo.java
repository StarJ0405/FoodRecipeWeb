package com.StarJ.food_recipe.Entities.Recipes.IngredientInfos;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class IngredientInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Recipe recipe;
    @ManyToOne(fetch = FetchType.LAZY)
    private Ingredient ingredient;
    private int amount;

    @Builder
    public IngredientInfo(Recipe recipe, Ingredient ingredient, int amount) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.amount = amount;
    }
}