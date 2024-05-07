package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Entities.Categories.CategoryService;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientService;
import com.StarJ.food_recipe.Entities.Nutrients.NutrientService;
import com.StarJ.food_recipe.Entities.PredictData.PredictDatumService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEval;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEvalService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeService;
import com.StarJ.food_recipe.Entities.Tags.TagService;
import com.StarJ.food_recipe.Entities.Tools.ToolService;
import com.StarJ.food_recipe.Entities.Units.UnitService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserService;
import com.StarJ.food_recipe.Global.initialDataService;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    private final initialDataService dataService;
    private final RecipeEvalService recipeEvalService;
    @GetMapping("/manager")
    public String managerHome(Model model) {
        model.addAttribute("categories", categoryService.getCount());
        model.addAttribute("users", userService.getCount());
        model.addAttribute("tools", toolService.getCount());
        model.addAttribute("units", unitService.getCount());
        model.addAttribute("nutrients", nutrientService.getCount());
        model.addAttribute("tags", tagService.getCount());
        model.addAttribute("ingredients", ingredientService.getCount());
        model.addAttribute("recipes", recipeService.getCount());
        model.addAttribute("evals", recipeEvalService.getCount());
        model.addAttribute("pds", predictDatumService.getCount());
        return "managers/home";
    }

    @PostMapping("/manager/initialData")
    public String training() {
        dataService.initial();
        return "redirect:/manager";
    }
    @PostMapping("/manager/resetData")
    public String reset() {
        dataService.resetAllData();
        return "redirect:/manager";
    }
    @PostMapping("/manager/initialTestData")
    public String testData() {
        dataService.createTestData();
        return "redirect:/manager";
    }
    @GetMapping("/init")
    public String init() {
        dataService.check();
        return "redirect:/user/login";
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
