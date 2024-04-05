package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private SiteUser author;
    private LocalDateTime createDate;
    @ManyToOne
    private SiteUser modifier;
    private LocalDateTime modifiedDate;
}
