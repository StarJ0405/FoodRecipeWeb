package com.StarJ.food_recipe.Entities.managers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/manager")
public class AdminController {
    //@AuthenticationPrincipal PrincipalDetail principalDetail
    //@PreAuthorize("isAuthenticated")

    @GetMapping("/category")
    public String categoires() {
        return "managers/category";
    }

    @GetMapping("/tag")
    public String tags() {
        return "managers/tag";
    }

    @GetMapping("/ingredient")
    public String ingredients() {
        return "managers/Ingredient";
    }

    @GetMapping("/recipe")
    public String recipes() {
        return "managers/recipe";
    }


}
