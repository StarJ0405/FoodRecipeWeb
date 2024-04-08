package com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class NutrientInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Ingredient ingredient;
    @ManyToOne(fetch = FetchType.LAZY)
    private Nutrient nutrient;
    private int amount;

    @Builder
    public NutrientInfo(Ingredient ingredient, Nutrient nutrient, int amount) {
        this.ingredient = ingredient;
        this.nutrient = nutrient;
        this.amount = amount;
    }
}
