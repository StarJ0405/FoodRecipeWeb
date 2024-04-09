package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImage;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tools.Tool;
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
    @OneToMany
    private List<Tag> tags;
    @OneToMany
    private List<Tool> tools;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;
    private LocalDateTime createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser modifier;
    private LocalDateTime modifiedDate;

    @Builder
    public Recipe(String subject, UUID uuid, List<Tag> tags, List<Tool> tools, SiteUser author) {
        this.subject = subject;
        this.UUID = uuid;
        this.bodyImages = new ArrayList<>();
        this.ingredientInfos = new ArrayList<>();
        this.tags = tags;
        this.tools = tools;
        this.author = author;
        this.createDate = LocalDateTime.now();
    }
}
