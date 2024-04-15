package com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.Form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NutrientInfoForm {
    @NotBlank(message = "영양분을 선택해주세요")
    private String nutrient;
    @NotNull(message = "양을 입력해주세요")
    private double amount;
}
