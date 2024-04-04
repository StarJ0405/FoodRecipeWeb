package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolRepository;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest
class FoodRecipeApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ToolRepository toolRepository;

    @Test
    void addData() {
        Optional<SiteUser> _user = userRepository.findById("admin");
        SiteUser user = _user.get();
        for (int i = 0; i < 500; i++) {
            Tool tool = Tool.builder().name(i + "번째 도구").author(user).description(i + "번째 도구에 대한 설명은 없습니다.").build();
            toolRepository.save(tool);
        }

    }

    @Test
    void UserModify() {
//        Optional<SiteUser> _user = userRepository.findById("admin");
        Optional<SiteUser> _user = userRepository.findById("admin");
        if (_user.isPresent()) {
            SiteUser user = _user.get();
            user.setPassword(passwordEncoder.encode(user.getId()));
//            user.setEmailVerified(true);
//                user.setRole(UserRole.ADMIN.getValue());
            userRepository.save(user);
        }
    }

}
