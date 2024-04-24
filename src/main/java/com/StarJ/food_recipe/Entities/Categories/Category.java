package com.StarJ.food_recipe.Entities.Categories;

import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;
    @Column(unique = true)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private SiteUser author;
    @Setter(AccessLevel.NONE)
    private LocalDateTime createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser modifier;
    private LocalDateTime modifiedDate;

    @Builder
    public Category(String name, SiteUser author) {
        this.name = name;
        this.author = author;
        this.createDate = LocalDateTime.now();
    }
}