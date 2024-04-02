package com.StarJ.food_recipe.Entities.Users;

import com.StarJ.food_recipe.Entities.Users.Form.FindIDForm;
import com.StarJ.food_recipe.Entities.Users.Form.SignupForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    //@AuthenticationPrincipal PrincipalDetail principalDetail
    @GetMapping("/login")
    public String login() {
        return "users/login";
    }


    @GetMapping("/signup")
    public String signup(SignupForm signupForm) {
        return "users/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignupForm signupForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "users/signup";
        if (!signupForm.getPassword1().equals(signupForm.getPassword2())) {
            bindingResult.rejectValue("password1", "passwordInMatch", "비밀번호1과 비밀번호2가 일치하지 않습니다.");
            return "users/signup";
        }
        if (userService.hasUser(signupForm.getId())) {
            bindingResult.reject("hasSameUser", "사용중인 아이디입니다.");
            return "users/signup";
        }
        userService.create(signupForm.getId(), signupForm.getPassword1(), signupForm.getNickname(), signupForm.getEmail());
        return "redirect:/";
    }
    @GetMapping("/findID")
    public String findID(FindIDForm findIDForm) {
        return "users/findID";
    }
    @PostMapping("/findID")
    public String findID(@Valid FindIDForm findIDForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "users/findID";

        return "users/login";
    }

    @GetMapping("/findPW")
    public String findPW() {
        return "users/findPW";
    }
}
