package com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.Form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IngredientInfoForm {
    @NotBlank(message = "재료를 선택해주세요")
    private String ingredient;
    @NotNull(message = "양을 입력해주세요")
    private int amount;
}
