package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class MainControllerAdvice {
    @ModelAttribute("user")
    public SiteUser getUser(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        return principalDetail != null ? principalDetail.getUser() : null;
    }
}
