package com.StarJ.food_recipe.Entities.Users;

import com.StarJ.food_recipe.Entities.Nutrients.NutrientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/manager")
public class userMangerController {
    private final UserService userService;

    @GetMapping("/user")
    public String users(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("paging", userService.getUsers(page));
        return "managers/users/user";
    }

}
