package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Entities.Categories.CategoryService;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientService;
import com.StarJ.food_recipe.Entities.Nutrients.NutrientService;
import com.StarJ.food_recipe.Entities.PredictData.PredictDatumService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeService;
import com.StarJ.food_recipe.Entities.Tags.TagService;
import com.StarJ.food_recipe.Entities.Tools.ToolService;
import com.StarJ.food_recipe.Entities.Units.UnitService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserService;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    //@AuthenticationPrincipal PrincipalDetail principalDetail
    //@PreAuthorize("isAuthenticated")
    private final CategoryService categoryService;
    private final PredictDatumService predictDatumService;
    private final UserService userService;
    private final ToolService toolService;
    private final UnitService unitService;
    private final NutrientService nutrientService;
    private final TagService tagService;
    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    @GetMapping("/manager")
    public String managerHome(Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("tools", toolService.getTools());
        model.addAttribute("units",unitService.getUnits());
        model.addAttribute("nutrients",nutrientService.getNutrients());
        model.addAttribute("tags",tagService.getTags());
        model.addAttribute("ingredients",ingredientService.getIngredients());
        model.addAttribute("recipes",recipeService.getRecipes());
        return "managers/home";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/recipe/list";
    }

    @GetMapping("/rank")
    public String rank(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        if (principalDetail != null) {
            SiteUser user = principalDetail.getUser();
            model.addAttribute("personal_top5", predictDatumService.getTop5(user));
        }
        model.addAttribute("top10", predictDatumService.getTop10());
        return "rank";
    }
}
