package com.StarJ.food_recipe.Entities.Configs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Config {
    @Id
    private String key_name;
    @Setter
    @Getter
    private String type;
    @Setter
    private String value;

    @Builder
    public Config(String key) {
        this.key_name = key;
    }

    public String getStringValue() {
        return this.value;
    }

    public Integer getIntegerValue() {
        try {
            return Integer.valueOf(this.value);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}
