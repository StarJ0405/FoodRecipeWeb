package com.StarJ.food_recipe.Entities.Units;

import com.StarJ.food_recipe.Entities.Units.Form.UnitEditForm;
import com.StarJ.food_recipe.Entities.Units.Unit;
import com.StarJ.food_recipe.Entities.Units.UnitService;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String post(Model model, @PathVariable("id") Integer name, UnitEditForm unitEditForm) {
        Unit unit = unitService.getUnit(name);
        unitEditForm.setName(unit.getName());
        unitEditForm.setDescription(unit.getDescription());
        return "managers/units/unit_edit";
    }

    @PostMapping("/unit/{id}")
    public String post(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id, @Param("name") String name, @Param("description") String description) {
        Unit unit = unitService.getUnit(id);
        unitService.modify(unit, principalDetail.getUser(), name, description);
        return "redirect:/manager/unit";
    }

    @GetMapping("/unit/delete/{id}")
    public String delete(Model model, @PathVariable("id") Integer id) {
        Unit unit = unitService.getUnit(id);
        unitService.delete(unit);
        return "redirect:/manager/unit";
    }

    @GetMapping("/unit/create")
    public String create(UnitEditForm unitEditForm) {
        return "managers/units/unit_create";
    }

    @PostMapping("/unit/create")
    public String create(@AuthenticationPrincipal PrincipalDetail principalDetail, UnitEditForm unitEditForm) {
        unitService.create(principalDetail.getUser(), unitEditForm.getName(), unitEditForm.getDescription());
        return "redirect:/manager/unit";
    }
}
