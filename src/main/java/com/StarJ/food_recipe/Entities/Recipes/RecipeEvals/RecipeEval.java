package com.StarJ.food_recipe.Entities.Recipes.RecipeEvals;

import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RecipeEval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser siteUser;
    @Min(0)
    @Max(5)
    private double val;
    @Setter(AccessLevel.NONE)
    private LocalDateTime createDate;

    @Builder
    public RecipeEval(Recipe recipe, SiteUser siteUser) {
        this.recipe = recipe;
        this.siteUser = siteUser;
        this.createDate = LocalDateTime.now();
    }
}
