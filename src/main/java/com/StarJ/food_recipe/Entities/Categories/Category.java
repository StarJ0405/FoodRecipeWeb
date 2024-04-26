package com.StarJ.food_recipe.Entities.Categories;

import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


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

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tagList;

    @Builder
    public Category(String name, SiteUser author) {
        this.name = name;
        this.author = author;
        this.createDate = LocalDateTime.now();
    }
}