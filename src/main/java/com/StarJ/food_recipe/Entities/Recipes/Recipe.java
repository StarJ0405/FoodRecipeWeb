package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImage;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEval;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTag;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTools.RecipeTool;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String subject;
    private UUID UUID;
    @Column(columnDefinition = "TEXT")
    private String baseImg;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BodyImage> bodyImages;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientInfo> ingredientInfos;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeTool> tools;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeTag> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;
    private LocalDateTime createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser modifier;
    private LocalDateTime modifiedDate;

    //    @OneToMany( mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeEval> evals;


    @Builder
    public Recipe(String subject, UUID uuid, SiteUser author) {
        this.subject = subject;
        this.UUID = uuid;
        this.bodyImages = new ArrayList<>();
        this.ingredientInfos = new ArrayList<>();
        this.author = author;
        this.createDate = LocalDateTime.now();
        this.evals = new ArrayList<>();
    }
}
