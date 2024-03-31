package com.StarJ.food_recipe.Entities.Users;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {
    @GetMapping("/login")
    public String login(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        return "login";
    }
}
