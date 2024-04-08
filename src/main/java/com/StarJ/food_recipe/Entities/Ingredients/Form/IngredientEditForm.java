package com.StarJ.food_recipe.Entities.Ingredients.Form;

import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.Form.NutrientInfoForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class IngredientEditForm {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotBlank(message = "정보를 입력해주세요")
    private String info;
    @NotNull(message = "열량을 입력해주세요")
    private Integer cal;
    @NotBlank(message = "단위를 입력해주세요")
    private String unit;

    private List<NutrientInfoForm> nutrientInfos;

}

