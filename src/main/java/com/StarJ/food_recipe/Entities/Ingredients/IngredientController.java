package com.StarJ.food_recipe.Entities.Ingredients;

import com.StarJ.food_recipe.Entities.Ingredients.Form.IngredientEditForm;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.Form.NutrientInfoForm;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import com.StarJ.food_recipe.Entities.Nutrients.NutrientService;
import com.StarJ.food_recipe.Entities.Units.Unit;
import com.StarJ.food_recipe.Entities.Units.UnitService;
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
@RequestMapping("/manager")
public class IngredientController {
    private final IngredientService ingredientService;
    private final UnitService unitService;
    private final NutrientService nutrientService;

    @ModelAttribute("units")
    public List<Unit> getUnits() {
        return unitService.getUnits();
    }

    @ModelAttribute("nutrients")
    public List<Nutrient> getNutrients() {
        return nutrientService.getNutrients();
    }

    @GetMapping("/ingredient")
    public String ingredients(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("paging", ingredientService.getIngredients(page));
        return "managers/ingredients/ingredient";
    }

    @GetMapping("/ingredient/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, IngredientEditForm ingredientEditForm) {
        Ingredient ingredient = ingredientService.getIngredient(id);
        ingredientEditForm.setName(ingredient.getName());
        ingredientEditForm.setInfo(ingredient.getInfo());
        ingredientEditForm.setCal(ingredient.getCal());
        ingredientEditForm.setUnit(ingredient.getUnit().getName());
        List<NutrientInfoForm> infos = new ArrayList<>();
        for (NutrientInfo info : ingredient.getNutrientInfos()) {
            NutrientInfoForm form = new NutrientInfoForm();
            form.setNutrient(info.getNutrient().getName());
            form.setAmount(info.getAmount());
            infos.add(form);
        }
        ingredientEditForm.setNutrientInfos(infos);
        model.addAttribute("destination", String.format("/manager/ingredient/%s", id));
        return "managers/ingredients/ingredient_post";
    }

    @PostMapping("/ingredient/{id}")
    public String edit(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id, @Valid IngredientEditForm ingredientEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", String.format("/manager/ingredient/%s", id));
            return "managers/ingredients/ingredient_post";
        }
        Ingredient ingredient = ingredientService.getIngredient(id);
        if (!ingredient.getName().equals(ingredientEditForm.getName()) && ingredientService.has(ingredientEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", String.format("/manager/ingredient/%s", id));
            return "managers/ingredients/ingredient_post";
        }
        List<NutrientInfoForm> nutrientInfoForms = ingredientEditForm.getNutrientInfos();
        ingredientEditForm.setNutrientInfos(nutrientInfoForms != null ? nutrientInfoForms.stream().filter(v -> v.getNutrient() != null).toList() : new ArrayList<>());
        ingredientService.modify(ingredient, principalDetail.getUser(), ingredientEditForm.getName(), ingredientEditForm.getInfo(), ingredientEditForm.getCal(), ingredientEditForm.getUnit(), ingredientEditForm.getNutrientInfos());
        return "redirect:/manager/ingredient";
    }

    @GetMapping("/ingredient/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Ingredient ingredient = ingredientService.getIngredient(id);
        ingredientService.delete(ingredient);
        return "redirect:/manager/ingredient";
    }

    @GetMapping("/ingredient/create")
    public String create(Model model, IngredientEditForm ingredientEditForm) {
        model.addAttribute("destination", "/manager/ingredient/create");
        return "managers/ingredients/ingredient_post";
    }

    @PostMapping("/ingredient/create")
    public String create(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @Valid IngredientEditForm ingredientEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", "/manager/ingredient/create");
            return "managers/ingredients/ingredient_post";
        }
        if (ingredientService.has(ingredientEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", "/manager/ingredient/create");
            return "managers/ingredients/ingredient_post";
        }
        List<NutrientInfoForm> nutrientInfoForms = ingredientEditForm.getNutrientInfos();
        ingredientEditForm.setNutrientInfos(nutrientInfoForms != null ? nutrientInfoForms.stream().filter(n -> n.getNutrient() != null && n.getAmount() > 0).toList() : new ArrayList<>());
        ingredientService.create(principalDetail.getUser(), ingredientEditForm.getName(), ingredientEditForm.getInfo(), ingredientEditForm.getCal(), ingredientEditForm.getUnit(), ingredientEditForm.getNutrientInfos());
        return "redirect:/manager/ingredient";
    }
}
