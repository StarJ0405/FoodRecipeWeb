package com.StarJ.food_recipe.Entities.Users;

import com.StarJ.food_recipe.Entities.Users.Form.LoginForm;
import com.StarJ.food_recipe.Entities.Users.Form.SignupForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {
    //@AuthenticationPrincipal PrincipalDetail principalDetail
    @GetMapping("/login")
    public String login(LoginForm loginForm) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginForm loginForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "login";
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signup(SignupForm signupForm) {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignupForm signupForm, BindingResult bindingResult) {
        System.out.print("try to singup");
        return "signup";
    }
}
