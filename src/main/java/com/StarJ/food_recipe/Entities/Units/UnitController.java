package com.StarJ.food_recipe.Entities.Units;

import com.StarJ.food_recipe.Entities.Units.Form.UnitEditForm;
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
public class UnitController {
    private final UnitService unitService;

    @GetMapping("/unit")
    public String units(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("paging", unitService.getUnits(page));
        return "managers/units/unit";
    }

    @GetMapping("/unit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, UnitEditForm unitEditForm) {
        Unit unit = unitService.getUnit(id);
        unitEditForm.setName(unit.getName());
        unitEditForm.setDescription(unit.getDescription());
        model.addAttribute("destination", String.format("/manager/unit/%s", id));
        return "managers/units/unit_post";
    }

    @PostMapping("/unit/{id}")
    public String edit(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id, @Valid UnitEditForm unitEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", String.format("/manager/unit/%s", id));
            return "managers/units/unit_post";
        }
        Unit unit = unitService.getUnit(id);
        if (!unit.getName().equals(unitEditForm.getName()) && unitService.has(unitEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", String.format("/manager/unit/%s", id));
            return "managers/units/unit_post";
        }


        unitService.modify(unit, principalDetail.getUser(), unitEditForm.getName(), unitEditForm.getDescription());
        return "redirect:/manager/unit";
    }

    @GetMapping("/unit/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Unit unit = unitService.getUnit(id);
        unitService.delete(unit);
        return "redirect:/manager/unit";
    }

    @GetMapping("/unit/create")
    public String create(Model model, UnitEditForm unitEditForm) {
        model.addAttribute("destination", "/manager/unit/create");
        return "managers/units/unit_post";
    }

    @PostMapping("/unit/create")
    public String create(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @Valid UnitEditForm unitEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", "/manager/unit/create");
            return "managers/units/unit_post";
        }
        if (unitService.has(unitEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", "/manager/unit/create");
            return "managers/units/unit_post";
        }
        unitService.create(principalDetail.getUser(), unitEditForm.getName(), unitEditForm.getDescription());
        return "redirect:/manager/unit";
    }
}
