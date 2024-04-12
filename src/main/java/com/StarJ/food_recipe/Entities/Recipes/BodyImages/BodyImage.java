package com.StarJ.food_recipe.Entities.Recipes.BodyImages;

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
public class BodyImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Recipe recipe;
    private String body;
    private String imgURL;

    @Builder
    public BodyImage(Recipe recipe, String body, String imgURL) {
        this.recipe = recipe;
        this.body = body;
        this.imgURL = imgURL;
    }
}
