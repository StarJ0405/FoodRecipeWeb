package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Recipes.BodyImages.Form.BodyImageForm;
import com.StarJ.food_recipe.Entities.Recipes.Form.RecipeEditForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.Form.IngredientInfoForm;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tags.TagService;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolService;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;
    private final TagService tagService;
    private final ToolService toolService;

    @ModelAttribute("tags")
    public List<Tag> getTags() {
        return tagService.getTags();
    }

    @ModelAttribute("tools")
    public List<Tool> getTools() {
        return toolService.getTools();
    }

    @GetMapping("/list")
    public String recipes(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("paging", recipeService.getRecipes(page));
        return "recipes/list";
    }

    @GetMapping("/create")
    public String create(Model model, RecipeEditForm recipeEditForm) {
        model.addAttribute("destination", "/recipe/create");
        return "recipes/create";
    }

    @PostMapping("/create")
    public String create(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @Valid RecipeEditForm recipeEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", "/recipe/create");
            return "recipes/create";
        }
        List<BodyImageForm> bodyImageForms = recipeEditForm.getBodyImages();
        recipeEditForm.setBodyImages(bodyImageForms != null ? bodyImageForms.stream().filter(img -> img.getBody() != null || img.getImgURL() != null).toList() : new ArrayList<>());

        List<IngredientInfoForm> ingredientInfoForms = recipeEditForm.getIngredientInfos();
        recipeEditForm.setIngredientInfos(ingredientInfoForms != null ? ingredientInfoForms.stream().filter(ing -> ing.getIngredient() != null && ing.getAmount() > 0).toList() : new ArrayList<>());
        recipeService.create(principalDetail.getUser(), recipeEditForm.getSubject(), "recipeEditForm.getBaseImg()", recipeEditForm.getTags(), recipeEditForm.getTools(), recipeEditForm.getBodyImages(), recipeEditForm.getIngredientInfos());
        return "recipes/create";
    }
}
