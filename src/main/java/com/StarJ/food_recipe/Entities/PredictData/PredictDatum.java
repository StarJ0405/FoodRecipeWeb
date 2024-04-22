package com.StarJ.food_recipe.Entities.PredictData;

import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PredictDatum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;
    @Min(0)
    @Max(5)
    private double predict_val;

    @Builder
    public PredictDatum(SiteUser user, Recipe recipe, double predict_val) {
        this.user = user;
        this.recipe = recipe;
        this.predict_val = predict_val;
    }
}
