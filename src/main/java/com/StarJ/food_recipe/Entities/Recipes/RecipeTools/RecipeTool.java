package com.StarJ.food_recipe.Entities.Recipes.RecipeTools;

import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RecipeTool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Recipe recipe;
    @ManyToOne
    private Tool tool;

    @Builder
    public RecipeTool(Recipe recipe, Tool tool) {
        this.recipe = recipe;
        this.tool = tool;
    }
}
