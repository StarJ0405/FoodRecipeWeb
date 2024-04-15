package com.StarJ.food_recipe.Entities.Ingredients;

import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo;
import com.StarJ.food_recipe.Entities.Units.Unit;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String info;
    private double kcal;
    @ManyToOne(fetch = FetchType.LAZY)
    private Unit unit;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutrientInfo> nutrientInfos;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;
    private LocalDateTime createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser modifier;
    private LocalDateTime modifiedDate;

    @Builder
    public Ingredient(String name, String info, double kcal, Unit unit, SiteUser author) {
        this.name = name;
        this.kcal = kcal;
        this.info = info;
        this.unit = unit;
        this.nutrientInfos = new ArrayList<>();
        this.author = author;
        this.createDate = LocalDateTime.now();
    }
}
