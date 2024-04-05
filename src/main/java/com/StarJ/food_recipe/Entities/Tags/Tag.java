package com.StarJ.food_recipe.Entities.Tags;

import com.StarJ.food_recipe.Entities.Categories.Category;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;

    @ManyToOne
    private Category category;
    @ManyToOne
    private SiteUser author;
    private LocalDateTime createDate;
    @ManyToOne
    private SiteUser modifier;
    private LocalDateTime modifiedDate;

    @Builder
    public Tag(String name, Category category, SiteUser author) {
        this.name = name;
        this.category = category;
        this.author = author;
        this.createDate = LocalDateTime.now();
    }
}
