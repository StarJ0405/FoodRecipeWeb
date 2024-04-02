package com.StarJ.food_recipe.Entities.Users.Form;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FindIDForm {
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;
}
