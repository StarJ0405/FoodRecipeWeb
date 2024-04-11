package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Entities.Categories.Category;
import com.StarJ.food_recipe.Entities.Categories.CategoryRepository;
import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientRepository;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfoRepository;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import com.StarJ.food_recipe.Entities.Nutrients.NutrientRepository;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tags.TagRepository;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolRepository;
import com.StarJ.food_recipe.Entities.Units.Unit;
import com.StarJ.food_recipe.Entities.Units.UnitRepository;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserRepository;
import com.StarJ.food_recipe.Securities.UserRole;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private NutrientInfoRepository nutrientInfoRepository;

    @Test
    void initialData() {
        SiteUser user = SiteUser.builder().id("user").password(passwordEncoder.encode("user")).email("ghdtjdwo126@naver.com").role(UserRole.USER.getValue()).nickname("사용자1").build();
        userRepository.save(user);
        user = SiteUser.builder().id("admin").password(passwordEncoder.encode("admin")).email("ghdtjdwo126@gmail.com").role(UserRole.ADMIN.getValue()).nickname("관리자").build();
        userRepository.save(user);
        List<Tool> tools = new ArrayList<>();
        List<Unit> units = new ArrayList<>();
        List<Nutrient> nutrients = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Tool tool = Tool.builder().author(user).name("도구" + i).description(i + "번째 도구").build();
            toolRepository.save(tool);
            tools.add(tool);
            Unit unit = Unit.builder().author(user).name("단위" + i).description(i + "번째 단위").build();
            unitRepository.save(unit);
            units.add(unit);
        }
        for (int i = 0; i < 100; i++) {
            Nutrient nutrient = Nutrient.builder().author(user).name("영양분" + i).description(i + "번째 영양분").build();
            nutrientRepository.save(nutrient);
            nutrients.add(nutrient);
        }
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = Category.builder().author(user).name("카테고리" + i).build();
            categoryRepository.save(category);
            for (int j = 0; j < 10; j++) {
                Tag tag = Tag.builder().author(user).category(category).name("태그" + i + "-" + j).build();
                tagRepository.save(tag);
                tags.add(tag);
            }
        }
        Random r= new Random();
        for(int i= 0; i<200;i++){
            Ingredient ingredient = Ingredient.builder().author(user).cal(r.nextInt()).unit(units.get(r.nextInt(units.size()))).name("재료"+i).info("정보").build();
            ingredientRepository.save(ingredient);
            List<NutrientInfo> nutrientInfos= new ArrayList<>();
            for(int j=0; j<1+r.nextInt(20);j++) {
                NutrientInfo nutrientInfo = NutrientInfo.builder().nutrient(nutrients.get(j)).ingredient(ingredient).amount(r.nextInt()).build();
                nutrientInfoRepository.save(nutrientInfo);
                nutrientInfos.add(nutrientInfo);
            }
            ingredient.setNutrientInfos(nutrientInfos);
            ingredientRepository.save(ingredient);
        }
    }


}
