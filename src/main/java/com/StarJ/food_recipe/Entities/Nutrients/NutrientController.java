package com.StarJ.food_recipe.Entities.Nutrients;

import com.StarJ.food_recipe.Entities.Nutrients.Form.NutrientEditForm;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/manager")
public class NutrientController {
    private final NutrientService nutrientService;

    @GetMapping("/nutrient")
    public String nutrients(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("paging", nutrientService.getNutrients(page));
        return "managers/nutrients/nutrient";
    }

    @GetMapping("/nutrient/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, NutrientEditForm nutrientEditForm) {
        Nutrient nutrient = nutrientService.getNutrient(id);
        nutrientEditForm.setName(nutrient.getName());
        nutrientEditForm.setDescription(nutrient.getDescription());
        model.addAttribute("destination", String.format("/manager/nutrient/%s", id));
        return "managers/nutrients/nutrient_post";
    }

    @PostMapping("/nutrient/{id}")
    public String edit(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id, @Valid NutrientEditForm nutrientEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", String.format("/manager/nutrient/%s", id));
            return "managers/nutrients/nutrient_post";
        }
        Nutrient nutrient = nutrientService.getNutrient(id);
        if (!nutrient.getName().equals(nutrientEditForm.getName()) && nutrientService.has(nutrientEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", String.format("/manager/nutrient/%s", id));
            return "managers/nutrients/nutrient_post";
        }

        nutrientService.modify(nutrient, principalDetail.getUser(), nutrientEditForm.getName(), nutrientEditForm.getDescription());
        return "redirect:/manager/nutrient";
    }

    @GetMapping("/nutrient/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Nutrient nutrient = nutrientService.getNutrient(id);
        nutrientService.delete(nutrient);
        return "redirect:/manager/nutrient";
    }

    @GetMapping("/nutrient/create")
    public String create(Model model, NutrientEditForm nutrientEditForm) {
        model.addAttribute("destination", "/manager/nutrient/create");
        return "managers/nutrients/nutrient_post";
    }

    @PostMapping("/nutrient/create")
    public String create(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @Valid NutrientEditForm nutrientEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", "/manager/nutrient/create");
            return "managers/nutrients/nutrient_post";
        }
        if (nutrientService.has(nutrientEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", "/manager/nutrient/create");
            return "managers/nutrients/nutrient_post";
        }
        nutrientService.create(principalDetail.getUser(), nutrientEditForm.getName(), nutrientEditForm.getDescription());
        return "redirect:/manager/nutrient";
    }
}
