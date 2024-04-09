package com.StarJ.food_recipe.Entities.Recipes.BodyImages.Form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BodyImageForm {
    @NotBlank(message = "내용을 입력해주세요")
    private String body;
    private String imgURL;
}
