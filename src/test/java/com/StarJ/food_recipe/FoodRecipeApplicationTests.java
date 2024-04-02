package com.StarJ.food_recipe;

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
    @Test
    void Test() {
        Optional<SiteUser> _user = userRepository.findById("admin");
        if(_user.isPresent()) {
            SiteUser user = _user.get();
//            user.setPassword(passwordEncoder.encode("admin"));
            user.setEmailVerified(true);

            userRepository.save(user);
        }
    }

}
