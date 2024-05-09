package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Categories.CategoryService;
import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientService;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import com.StarJ.food_recipe.Entities.PredictData.PredictDatumService;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImage;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.Form.BodyImageForm;
import com.StarJ.food_recipe.Entities.Recipes.Form.RecipeEditForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.Form.IngredientInfoForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEval;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEvalService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTag;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTools.RecipeTool;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tags.TagService;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;
    private final TagService tagService;
    private final ToolService toolService;
    private final IngredientService ingredientService;
    private final RecipeEvalService recipeEvalService;
    private final PredictDatumService predictDatumService;
    private final CategoryService categoryService;

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

    private String StringArraytoString(String[] strs) {
        StringBuilder value = new StringBuilder();
        for (String str : strs)
            if (value.isEmpty())
                value.append(str);
            else
                value.append(", ").append(str);

        return value.toString();
    }

    @GetMapping("/list")
    public String recipes(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "searchKW", defaultValue = "") String searchKW, @RequestParam(value = "tags", required = false) String[] tags) {
        model.addAttribute("paging", recipeService.getRecipes(page, searchKW, tags));
        model.addAttribute("searchKW", searchKW);
        model.addAttribute("tags", tags);
        model.addAttribute("categories", categoryService.getCategories());
        return "recipes/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id) {
        Recipe recipe = recipeService.getRecipe(id);
        model.addAttribute("recipe", recipe);
        if (principalDetail != null)
            model.addAttribute("user", principalDetail.getUser());
        long totalCal = 0l;
        LinkedHashMap<Nutrient, Double> nutrients = new LinkedHashMap<>();
        for (IngredientInfo ingredientInfo : recipe.getIngredientInfos()) {
            totalCal += ingredientInfo.getIngredient().getKcal() * ingredientInfo.getAmount();
            for (NutrientInfo info : ingredientInfo.getIngredient().getNutrientInfos()) {
                Nutrient nutrient = info.getNutrient();
                if (!nutrients.containsKey(nutrient))
                    nutrients.put(nutrient, info.getAmount());
                else
                    nutrients.put(nutrient, nutrients.get(nutrient) + info.getAmount());
            }
        }
        double eval = Math.ceil(recipe.getEvals().stream().mapToDouble(a -> a.getVal()).average().orElse(0d) * 100d) / 100d;
        RecipeEval recipeEval = principalDetail != null ? recipeEvalService.getEval(principalDetail.getUser(), recipe) : null;
        double pre = recipeEval != null ? recipeEval.getVal() : -1;

        model.addAttribute("totalCal", new DecimalFormat("###,###").format(totalCal));
        model.addAttribute("nutrients", nutrients);
        model.addAttribute("eval", eval);
        model.addAttribute("rating", getRating(eval));
        model.addAttribute("pre", pre);
        model.addAttribute("preRating", pre >= 0 ? getRating(pre) : "");
        return "recipes/detail";
    }

    @PostMapping("/detail/{id}")
    public String rating(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id, @RequestParam("rating-10") Double rating) {
        SiteUser user = principalDetail.getUser();
        Recipe recipe = recipeService.getRecipe(id);
        recipeEvalService.setEval(user, recipe, rating);
//        predictDatumService.training();
        return "redirect:/recipe/detail/" + id;
    }

    private String getRating(double eval) {
        if (eval < 0.5)
            return "00";
        else if (eval < 1)
            return "05";
        else if (eval < 1.5)
            return "10";
        else if (eval < 2)
            return "15";
        else if (eval < 2.5)
            return "20";
        else if (eval < 3)
            return "25";
        else if (eval < 3.5)
            return "30";
        else if (eval < 4)
            return "35";
        else if (eval < 4.5)
            return "40";
        else if (eval < 5)
            return "45";
        else
            return "50";
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, RecipeEditForm recipeEditForm) {
        if (!model.containsAttribute("preRecipeEditForm")) {
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
        } else
            model.addAttribute("recipeEditForm", model.getAttribute("preRecipeEditForm"));

        if (!model.containsAttribute("imgUrl")) {
            String url = recipeEditForm.getBaseImg();
            model.addAttribute("imgUrl", url);
        }
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
        List<IngredientInfoForm> ingredientInfoForms = recipeEditForm.getIngredientInfos();
        recipeEditForm.setIngredientInfos(ingredientInfoForms != null ? ingredientInfoForms.stream().filter(ing -> ing.getIngredient() != null && ing.getAmount() > 0).toList() : new ArrayList<>());
        recipeService.edit(recipe, principalDetail.getUser(), recipeEditForm.getSubject(), recipeEditForm.getBaseImg(), recipeEditForm.getTags(), recipeEditForm.getTools(), recipeEditForm.getBodyImages(), recipeEditForm.getIngredientInfos());
        return "redirect:/recipe/list";
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/create")
    public String create(Model model, RecipeEditForm recipeEditForm) {
        if (model.containsAttribute("preRecipeEditForm"))
            model.addAttribute("recipeEditForm", model.getAttribute("preRecipeEditForm"));
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
    public String tempImg(Model model, @RequestParam("destination") String destination, @AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam("tempImg") MultipartFile file, RecipeEditForm recipeEditForm, RedirectAttributes rtti) {
        List<BodyImageForm> bodyImageForms = recipeEditForm.getBodyImages();
        recipeEditForm.setBodyImages(bodyImageForms != null ? bodyImageForms.stream().filter(img -> img.getBody() != null || img.getImgURL() != null).toList() : new ArrayList<>());

        List<IngredientInfoForm> ingredientInfoForms = recipeEditForm.getIngredientInfos();
        recipeEditForm.setIngredientInfos(ingredientInfoForms != null ? ingredientInfoForms.stream().filter(ing -> ing.getIngredient() != null && ing.getAmount() > 0).toList() : new ArrayList<>());

        String url = null;
        if (file != null && file.getContentType().contains("image"))
            url = recipeService.saveTempImage(file, principalDetail.getUser());
        rtti.addFlashAttribute("imgUrl", url);
        rtti.addFlashAttribute("preRecipeEditForm", recipeEditForm);
        return String.format("redirect:%s", destination);
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/bodyImg")
    public String tempBodyImg(Model model, @RequestParam("destination") String destination, @AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam("tempImg") MultipartFile file, @RequestParam("temp") String temp, RecipeEditForm recipeEditForm, RedirectAttributes rtti) {
        Integer number = null;
        try {
            number = Integer.parseInt(temp.split("\\[")[1].split("]")[0]);
        } catch (NumberFormatException nfe) {

        }
        String url = null;
        if (file != null && file.getContentType().contains("image"))
            url = recipeService.saveTempBodyImage(file, principalDetail.getUser());
        List<BodyImageForm> bodyImageForms = recipeEditForm.getBodyImages();
        BodyImageForm bodyImageForm = bodyImageForms.get(number);
        bodyImageForm.setImgURL(url);
        bodyImageForms.set(number, bodyImageForm);
        recipeEditForm.setBodyImages(bodyImageForms != null ? bodyImageForms.stream().filter(img -> img.getBody() != null || img.getImgURL() != null).toList() : new ArrayList<>());

        List<IngredientInfoForm> ingredientInfoForms = recipeEditForm.getIngredientInfos();
        recipeEditForm.setIngredientInfos(ingredientInfoForms != null ? ingredientInfoForms.stream().filter(ing -> ing.getIngredient() != null && ing.getAmount() > 0).toList() : new ArrayList<>());

        rtti.addFlashAttribute("imgUrl", recipeEditForm.getBaseImg());
        rtti.addFlashAttribute("preRecipeEditForm", recipeEditForm);
        return String.format("redirect:%s", destination);
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Recipe recipe = recipeService.getRecipe(id);
        recipeService.delete(recipe);
        return "redirect:/recipe/list";
    }
}
