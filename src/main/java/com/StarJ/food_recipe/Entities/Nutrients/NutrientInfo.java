package com.StarJ.food_recipe.Entities.Nutrients;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class NutrientInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Nutrient nutrient;
    private int amount;

    @Builder
    public NutrientInfo(Nutrient nutrient, int amount) {
        this.nutrient = nutrient;
        this.amount = amount;
    }
}
