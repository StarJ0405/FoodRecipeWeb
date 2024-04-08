package com.StarJ.food_recipe.Entities.Nutrients;

import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Nutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;
    private LocalDateTime createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser modifier;
    private LocalDateTime modifiedDate;

    @Builder
    public Nutrient(String name, String description, SiteUser author) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.createDate = LocalDateTime.now();
    }
}