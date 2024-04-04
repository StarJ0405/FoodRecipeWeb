package com.StarJ.food_recipe.Entities.Units;

import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Unit {
    @Id
    private Integer id;
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    private SiteUser author;
    private LocalDateTime createDate;
    @ManyToOne
    private SiteUser modifier;
    private LocalDateTime modifiedDate;

    @Builder
    public Unit(String name, String description, SiteUser author) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.createDate = LocalDateTime.now();
    }
}
