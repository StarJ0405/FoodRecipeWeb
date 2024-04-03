package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Securities.PrincipalDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }
}
