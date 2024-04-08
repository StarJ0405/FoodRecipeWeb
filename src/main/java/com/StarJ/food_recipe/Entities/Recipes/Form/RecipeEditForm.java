package com.StarJ.food_recipe.Entities.Recipes.Form;

import com.StarJ.food_recipe.Entities.Recipes.BodyImages.Form.BodyImageForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.Form.IngredientInfoForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RecipeEditForm {
    @NotBlank(message = "제목을 입력해주세요")
    private String subject;

    @NotNull(message = "기본 이미지를 입력해주세요")
    private MultipartFile baseImg;

    private List<BodyImageForm> bodyImages;
    private List<IngredientInfoForm> ingredientInfos;
    private List<String> tags;
    private List<String> tools;
}

