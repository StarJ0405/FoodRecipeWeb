package com.StarJ.food_recipe.Entities.Users;

import com.StarJ.food_recipe.Entities.Users.Form.*;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
    private final UserService userService;

    //@AuthenticationPrincipal PrincipalDetail principalDetail
    //@PreAuthorize("isAuthenticated")
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
        if (userService.hasEmail(signupForm.getEmail())) {
            bindingResult.reject("hasSameEmail", "등록된 이메일주소입니다.");
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
        if (bindingResult.hasErrors())
            return "users/findID";
        SiteUser user = userService.getUserbyEmail(findIDForm.getEmail());
        if (user == null) {
            bindingResult.rejectValue("email", "userNotExist", "해당하는 아이디가 없는 이메일주소입니다.");
            return "users/findID";
        }
        userService.sendIDEmail(user);
        return "redirect:/user/login";
    }

    @GetMapping("/findPW")
    public String findPW(FindPWForm findPWForm) {
        return "users/findPW";
    }

    @PostMapping("/findPW")
    public String findPW(@Valid FindPWForm findPWForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "users/findPW";
        SiteUser user = userService.getUserbyID(findPWForm.getId());
        if (user == null) {
            bindingResult.rejectValue("id", "userNotExist", "잘못된 아이디입니다.");
            return "users/findPW";
        }
        if (!user.getEmail().equals(findPWForm.getEmail())) {
            bindingResult.rejectValue("email", "notSameEmail", "잘못된 이메일 주소입니다.");
            return "users/findPW";
        }
        userService.sendPWEmail(user);
        return "redirect:/user/login";
    }

    //    @PreAuthorize("isAuthenticated")
    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        model.addAttribute("iconUrl", principalDetail.getUser().getIconUrl());
        return "users/profile";
    }

    @PostMapping("/profile")
    public String profile(@AuthenticationPrincipal PrincipalDetail principalDetail, @Param("url") String url, @Param("nickname") String nickname) {
        userService.changeProfile(principalDetail.getUser(), url, nickname);
        return "redirect:/user/profile";
    }


    @PostMapping("/profile/image")
    public String profileImage(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam MultipartFile file) {
        String url = userService.saveTempImage(file, principalDetail.getUser());
        if (url != null)
            model.addAttribute("iconUrl", url);
        return "/users/profile";
    }

    @GetMapping("/changePassword")
    public String changePassword(ChangePasswordForm changePasswordForm) {
        return "users/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@AuthenticationPrincipal PrincipalDetail principalDetail, @Valid ChangePasswordForm changePasswordForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "users/changePassword";
        SiteUser user = principalDetail.getUser();
        if (!userService.isSamePassword(user, changePasswordForm.getPassword())) {
            bindingResult.rejectValue("password", "notCorrectPassword", "비밀번호가 틀립니다.");
            return "users/changePassword";
        }
        if (!changePasswordForm.getPassword1().equals(changePasswordForm.getPassword2())) {
            bindingResult.rejectValue("password2", "notSamePasswords", "비밀번호가 틀립니다.");
            return "users/changePassword";
        }
        userService.changePassword(user, changePasswordForm.getPassword1());
        return "redirect:/user/profile";
    }

    @GetMapping("/changeEmail")
    public String changeEmail(ChangeEmailForm changeEmailForm) {
        return "users/changeEmail";
    }


    @PostMapping("/changeEmail")
    public String changeEmail(@AuthenticationPrincipal PrincipalDetail principalDetail, @Valid ChangeEmailForm changeEmailForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "users/changeEmail";
        SiteUser user = principalDetail.getUser();
        if (!userService.isSamePassword(user, changeEmailForm.getPassword())) {
            bindingResult.rejectValue("password", "notCorrectPassword", "비밀번호가 틀립니다.");
            return "users/changePassword";
        }
        if (!user.getEmail().equals(changeEmailForm.getEmail())) {
            bindingResult.rejectValue("email", "notCorrectEmail", "이메일이 틀립니다.");
            return "users/changeEmail";
        }
        SiteUser checkUser = userService.getUserbyEmail(changeEmailForm.getNewEmail());
        if (checkUser != null) {
            bindingResult.rejectValue("email", "notCorrectEmail", "이미 사용중인 이메일입니다.");
            return "users/changeEmail";
        }
        userService.changeEmail(user, changeEmailForm.getNewEmail());
        return "redirect:/user/profile";
    }
}
