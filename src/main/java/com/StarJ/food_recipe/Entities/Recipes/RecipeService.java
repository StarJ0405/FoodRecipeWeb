package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientService;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImage;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImageService;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.Form.BodyImageForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.Form.IngredientInfoForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfoService;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tags.TagService;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final TagService tagService;
    private final ToolService toolService;
    private final BodyImageService bodyImageService;
    private final IngredientService ingredientService;
    private final IngredientInfoService ingredientInfoService;

    public Page<Recipe> getRecipes(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));
        return recipeRepository.findAll(pageable);
    }

    public void modify(Recipe recipe, SiteUser user, String subject, String baseImg, List<String> _tags, List<String> _tools, List<BodyImageForm> bodyImageForms, List<IngredientInfoForm> ingredientInfoForms) {
        recipe.setModifier(user);
        recipe.setModifiedDate(LocalDateTime.now());
        recipe.setSubject(subject);
        recipe.setBaseImg(baseImg);
        recipe.setTags( tagService.getTags(_tags));
        recipe.setTools(toolService.getTools(_tools));
        List<BodyImage> bodyImages = recipe.getBodyImages();
        bodyImages.clear();
        for (BodyImageForm form : bodyImageForms) {
            BodyImage bodyImage = bodyImageService.getBodyImage(recipe, form.getBody(), form.getImgURL());
            if (bodyImage != null)
                bodyImages.add(bodyImage);
        }
        List<IngredientInfo> ingredientInfos = recipe.getIngredientInfos();
        ingredientInfos.clear();
        for (IngredientInfoForm form : ingredientInfoForms) {
            Ingredient ingredient = ingredientService.getIngredient(form.getIngredient());
            IngredientInfo ingredientInfo = ingredientInfoService.getModifiedIngredientInfo(recipe, ingredient, form.getAmount());
            if (ingredientInfo != null)
                ingredientInfos.add(ingredientInfo);
        }
        recipeRepository.save(recipe);
    }

    public boolean has(int id) {
        return recipeRepository.findById(id).isPresent();
    }

    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    @Transactional
    public void create(SiteUser user, String subject, String baseImg, List<String> _tags, List<String> _tools, List<BodyImageForm> bodyImageForms, List<IngredientInfoForm> ingredientInfoForms) {
        List<Tag> tags = tagService.getTags(_tags);
        List<Tool> tools = toolService.getTools(_tools);
        Recipe recipe = Recipe.builder().author(user).subject(subject).baseImg(baseImg).tags(tags).tools(tools).build();
        List<BodyImage> bodyImages = recipe.getBodyImages();
        for (BodyImageForm form : bodyImageForms) {
            BodyImage bodyImage = bodyImageService.getBodyImage(recipe, form.getBody(), form.getImgURL());
            if (bodyImage != null)
                bodyImages.add(bodyImage);
        }
        List<IngredientInfo> ingredientInfos = recipe.getIngredientInfos();
        for (IngredientInfoForm form : ingredientInfoForms) {
            Ingredient ingredient = ingredientService.getIngredient(form.getIngredient());
            IngredientInfo ingredientInfo = ingredientInfoService.getIngredientInfo(recipe, ingredient, form.getAmount());
            if (ingredientInfo != null)
                ingredientInfos.add(ingredientInfo);
        }
        recipeRepository.save(recipe);

    }
}
