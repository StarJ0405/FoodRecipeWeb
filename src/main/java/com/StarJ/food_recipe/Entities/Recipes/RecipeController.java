package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientService;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImage;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.Form.BodyImageForm;
import com.StarJ.food_recipe.Entities.Recipes.Form.RecipeEditForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.Form.IngredientInfoForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTag;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTools.RecipeTool;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tags.TagService;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolService;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;
    private final TagService tagService;
    private final ToolService toolService;
    private final IngredientService ingredientService;

    @ModelAttribute("ingredients")
    public List<Ingredient> getIngredients() {
        return ingredientService.getIngredients();
    }

    @ModelAttribute("tags")
    public List<Tag> getTags() {
        return tagService.getTags();
    }

    @ModelAttribute("tools")
    public List<Tool> getTools() {
        return toolService.getTools();
    }

    @GetMapping("")
    public String home() {
        return "redirect:/recipe/list";
    }

    @GetMapping("/list")
    public String recipes(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("paging", recipeService.getRecipes(page));
        return "recipes/list";
    }


    @PreAuthorize("isAuthenticated")
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, RecipeEditForm recipeEditForm) {
        Recipe recipe = recipeService.getRecipe(id);
        recipeEditForm.setSubject(recipe.getSubject());
        recipeEditForm.setBaseImg(recipe.getBaseImg());
        List<BodyImageForm> bodyImageForms = new ArrayList<>();
        for (BodyImage bodyImage : recipe.getBodyImages())
            bodyImageForms.add(new BodyImageForm(bodyImage.getBody(), bodyImage.getImgURL()));
        recipeEditForm.setBodyImages(bodyImageForms);
        List<IngredientInfoForm> ingredientInfoForms = new ArrayList<>();
        for (IngredientInfo ingredientInfo : recipe.getIngredientInfos())
            ingredientInfoForms.add(new IngredientInfoForm(ingredientInfo.getIngredient().getName(), ingredientInfo.getAmount()));
        recipeEditForm.setIngredientInfos(ingredientInfoForms);
        List<String> tags = new ArrayList<>();
        for (RecipeTag recipeTag : recipe.getTags())
            tags.add(recipeTag.getTag().getName());
        recipeEditForm.setTags(tags);
        List<String> tools = new ArrayList<>();
        for (RecipeTool recipeTool : recipe.getTools())
            tools.add(recipeTool.getTool().getName());
        recipeEditForm.setTools(tools);
        String url = recipeEditForm.getBaseImg();
        model.addAttribute("imgUrl", url);
        model.addAttribute("destination", String.format("/recipe/edit/%s", id));
        return "recipes/post";
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/edit/{id}")
    public String edit(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id, @Valid RecipeEditForm recipeEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", String.format("/recipe/edit/%s", id));
            return "recipes/post";
        }
        Recipe recipe = recipeService.getRecipe(id);
        List<BodyImageForm> bodyImageForms = recipeEditForm.getBodyImages();

        recipeEditForm.setBodyImages(bodyImageForms != null ? bodyImageForms.stream().filter(img -> img.getBody() != null || img.getImgURL() != null).toList() : new ArrayList<>());

        System.out.println(recipeEditForm.getBodyImages().size());
        for(BodyImageForm bodyImageForm : recipeEditForm.getBodyImages())
            System.out.println(bodyImageForm.getBody());

        List<IngredientInfoForm> ingredientInfoForms = recipeEditForm.getIngredientInfos();
        recipeEditForm.setIngredientInfos(ingredientInfoForms != null ? ingredientInfoForms.stream().filter(ing -> ing.getIngredient() != null && ing.getAmount() > 0).toList() : new ArrayList<>());
        recipeService.edit(recipe, principalDetail.getUser(), recipeEditForm.getSubject(), recipeEditForm.getBaseImg(), recipeEditForm.getTags(), recipeEditForm.getTools(), recipeEditForm.getBodyImages(), recipeEditForm.getIngredientInfos());
        return "redirect:/recipe/list";
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/create")
    public String create(Model model, RecipeEditForm recipeEditForm) {
        model.addAttribute("destination", "/recipe/create");
        return "recipes/post";
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/create")
    public String create(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @Valid RecipeEditForm recipeEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", "/recipe/create");
            return "recipes/post";
        }
        List<BodyImageForm> bodyImageForms = recipeEditForm.getBodyImages();
        recipeEditForm.setBodyImages(bodyImageForms != null ? bodyImageForms.stream().filter(img -> img.getBody() != null || img.getImgURL() != null).toList() : new ArrayList<>());

        List<IngredientInfoForm> ingredientInfoForms = recipeEditForm.getIngredientInfos();
        recipeEditForm.setIngredientInfos(ingredientInfoForms != null ? ingredientInfoForms.stream().filter(ing -> ing.getIngredient() != null && ing.getAmount() > 0).toList() : new ArrayList<>());
        recipeService.create(principalDetail.getUser(), recipeEditForm.getSubject(), recipeEditForm.getBaseImg(), recipeEditForm.getTags(), recipeEditForm.getTools(), recipeEditForm.getBodyImages(), recipeEditForm.getIngredientInfos());
        return "redirect:/recipe/list";
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/baseImg")
    public String tempImg(Model model, @RequestParam("destination") String destination, @AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam("tempImg") MultipartFile file,
                          @RequestParam("subject") String subject, @RequestParam(value = "ingredient", required = false) List<String> ingredients, @RequestParam(value = "ingredient_amount", required = false) List<Integer> ingredient_amounts
            , @RequestParam(value = "tools", required = false) List<String> tools,@RequestParam(value="tags") List<String> tags) {
        String url = null;
        if (file != null && file.getContentType().contains("image"))
            url = recipeService.saveTempImage(file, principalDetail.getUser());
        model.addAttribute("imgUrl", url);
        model.addAttribute("destination", destination);

        RecipeEditForm recipeEditForm = new RecipeEditForm();
        recipeEditForm.setSubject(subject);
        List<IngredientInfoForm> ingredientInfos = new ArrayList<>();
        if (ingredients != null)
            for (int i = 0; i < ingredients.size(); i++)
                ingredientInfos.add(new IngredientInfoForm(ingredients.get(i), ingredient_amounts.get(i)));
        recipeEditForm.setIngredientInfos(ingredientInfos);
        recipeEditForm.setTools(tools);
        recipeEditForm.setTags(tags);
        model.addAttribute("recipeEditForm", recipeEditForm);
        return "recipes/post";
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Recipe recipe = recipeService.getRecipe(id);
        recipeService.delete(recipe);
        return "redirect:/recipe/list";
    }
}
