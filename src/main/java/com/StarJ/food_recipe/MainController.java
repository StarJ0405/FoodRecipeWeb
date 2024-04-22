package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Entities.PredictData.PredictDatumService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserService;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    //@AuthenticationPrincipal PrincipalDetail principalDetail
    //@PreAuthorize("isAuthenticated")
    private final PredictDatumService predictDatumService;
    private final UserService userService;

    @GetMapping("/manager")
    public String managerHome() {
        return "managers/home";
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        if (principalDetail != null) {
            SiteUser user = principalDetail.getUser();
            model.addAttribute("predictList", predictDatumService.getTop5(user));
        }
        return "home";
    }
}
