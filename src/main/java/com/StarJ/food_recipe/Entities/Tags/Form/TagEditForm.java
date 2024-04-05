package com.StarJ.food_recipe.Entities.Tags.Form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagEditForm {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    private String category;
}
