package com.StarJ.food_recipe.Entities.Nutrients.Form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NutrientEditForm {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    private String description;
}
