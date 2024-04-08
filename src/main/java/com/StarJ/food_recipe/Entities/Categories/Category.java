package com.StarJ.food_recipe.Entities.Categories;

import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;
    private LocalDateTime createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser modifier;
    private LocalDateTime modifiedDate;

    @Builder
    public Category(String name, SiteUser author, LocalDateTime createDate) {
        this.name = name;
        this.author = author;
        this.createDate = createDate;
    }
}