package com.StarJ.food_recipe.Entities.Users.Form;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupForm {
    @Pattern(regexp = "^[a-z0-9-_]{3,16}$", message = "아이디는 알파벳 및 숫자로 이루어진 3 ~ 16자리여야 합니다.")
    private String id;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password1;
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password2;

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{3,16}$", message = "닉네임은 특수문자를 제외한 3 ~ 16자리여야 합니다.")
    private String nickname;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;
}
