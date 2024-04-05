package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Entities.Ingredients.IngredientRepository;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import com.StarJ.food_recipe.Entities.Nutrients.NutrientRepository;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolRepository;
import com.StarJ.food_recipe.Entities.Units.Unit;
import com.StarJ.food_recipe.Entities.Units.UnitRepository;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserRepository;
import com.StarJ.food_recipe.Securities.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class FoodRecipeApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ToolRepository toolRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private NutrientRepository nutrientRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void initialData() {
        SiteUser user = SiteUser.builder().id("user").password(passwordEncoder.encode("user")).email("ghdtjdwo126@naver.com").role(UserRole.USER.getValue()).nickname("사용자1").build();
        userRepository.save(user);
        user = SiteUser.builder().id("admin").password(passwordEncoder.encode("admin")).email("ghdtjdwo126@gmail.com").role(UserRole.ADMIN.getValue()).nickname("관리자").build();
        userRepository.save(user);
        for (int i = 0; i < 500; i++) {
            Tool tool = Tool.builder().author(user).name("도구" + i).description(i + "번째 도구").build();
            toolRepository.save(tool);
            Unit unit = Unit.builder().author(user).name("단위" + i).description(i + "번째 단위").build();
            unitRepository.save(unit);
            Nutrient nutrient = Nutrient.builder().author(user).name("영양분" + i).description(i + "번째 영양분").build();
            nutrientRepository.save(nutrient);
        }


    }


}
