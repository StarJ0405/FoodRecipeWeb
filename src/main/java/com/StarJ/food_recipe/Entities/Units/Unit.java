package com.StarJ.food_recipe.Entities.Units;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Unit {
    @Id
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
}
