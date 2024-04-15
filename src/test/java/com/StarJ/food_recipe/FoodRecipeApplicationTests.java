package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Entities.Categories.Category;
import com.StarJ.food_recipe.Entities.Categories.CategoryRepository;
import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientRepository;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfoRepository;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import com.StarJ.food_recipe.Entities.Nutrients.NutrientRepository;
import com.StarJ.food_recipe.Entities.Recipes.Recipe;
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
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    //&#10;
    @Test
    void initialData() {
        try {
            String filePath = "C:/Users/admin/IdeaProjects/FoodRecipeWeb/initialData.xlsx";
            Workbook workbook = WorkbookFactory.create(new File(filePath));

            List<SiteUser> users = initialUsers(workbook.getSheet("users"));
            SiteUser admin = null;
            for (SiteUser user : users)
                if (user.getRole().equals(UserRole.ADMIN.getValue())) {
                    admin = user;
                    break;
                }
            List<Tool> tools = initialTools(admin, workbook.getSheet("tools"));
            List<Unit> units = initialUnits(admin, workbook.getSheet("units"));
            List<Nutrient> nutrients = initialNutrients(admin, workbook.getSheet("nutrients"));
            List<Category> categories = initialCategories(admin, workbook.getSheet("categories"));
            List<Tag> tags = initialTags(admin, workbook.getSheet("tags"));
            List<Ingredient> ingredients = initialIngredients(admin, workbook.getSheet("ingredients"));
            workbook.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.print("IOException");
        }
    }

    List<SiteUser> initialUsers(Sheet userSheet) {
        List<SiteUser> list = new ArrayList<>();
        for (Row row : userSheet) {
            String id = row.getCell(0).getStringCellValue();
            String role = row.getCell(1).getStringCellValue();
            String nickname = row.getCell(2).getStringCellValue();
            row.getCell(3).setCellType(CellType.STRING);
            String password = row.getCell(3).getStringCellValue();
            SiteUser user = SiteUser.builder().id(id).role(role).nickname(nickname).password(passwordEncoder.encode(password)).build();
            userRepository.save(user);
            list.add(user);
        }
        return list;
    }

    List<Tool> initialTools(SiteUser admin, Sheet toolSheet) {
        List<Tool> list = new ArrayList<>();
        for (Row row : toolSheet) {
            String name = null;
            String description = "";
            for (Cell cell : row)
                if (name == null)
                    name = cell.getStringCellValue();
                else
                    description += (description.equals("") ? "" : "\n") + cell.getStringCellValue();
            Tool tool = Tool.builder().author(admin).name(name).description(description).build();
            toolRepository.save(tool);
            list.add(tool);
        }
        return list;
    }

    List<Unit> initialUnits(SiteUser admin, Sheet unitSheet) {
        List<Unit> list = new ArrayList<>();
        for (Row row : unitSheet) {
            String name = null;
            String description = "";
            for (Cell cell : row)
                if (name == null)
                    name = cell.getStringCellValue();
                else
                    description += (description.equals("") ? "" : "\n") + cell.getStringCellValue();
            Unit unit = Unit.builder().author(admin).name(name).description(description).build();
            unitRepository.save(unit);
            list.add(unit);
        }
        return list;
    }

    List<Nutrient> initialNutrients(SiteUser admin, Sheet nutrientSheet) {
        List<Nutrient> list = new ArrayList<>();
        for (Row row : nutrientSheet) {
            String name = null;
            String description = "";
            for (Cell cell : row)
                if (name == null)
                    name = cell.getStringCellValue();
                else
                    description += (description.equals("") ? "" : "\n") + cell.getStringCellValue();
            Nutrient nutrient = Nutrient.builder().author(admin).name(name).description(description).build();
            nutrientRepository.save(nutrient);
            list.add(nutrient);
        }
        return list;
    }

    List<Category> initialCategories(SiteUser admin, Sheet categorySheet) {
        List<Category> list = new ArrayList<>();
        for (Row row : categorySheet)
            for (Cell cell : row) {
                Category category = Category.builder().author(admin).name(cell.getStringCellValue()).build();
                categoryRepository.save(category);
                list.add(category);
            }
        return list;
    }

    List<Tag> initialTags(SiteUser admin, Sheet tagSheet) {
        List<Tag> list = new ArrayList<>();
        for (Row row : tagSheet) {
            String name = row.getCell(0).getStringCellValue();
            String category_name = row.getCell(1).getStringCellValue();
            Optional<Category> _category = categoryRepository.findById(name);
            Category category = _category.isPresent() ? _category.get() : null;

            Tag tag = Tag.builder().author(admin).name(name).category(category).build();
            tagRepository.save(tag);
            list.add(tag);
        }
        return list;
    }

    @Transactional
    List<Ingredient> initialIngredients(SiteUser admin, Sheet ingredientSheet) {
        List<Ingredient> list = new ArrayList<>();
        for (Row row : ingredientSheet) {
            if (row.getRowNum() == 0)
                continue;
            String name = null;
            String info = null;
            double kcal = 0d;
            Unit unit = null;
            HashMap<Nutrient, Double> nutrientMap = new HashMap();
            for (Cell cell : row)
                switch (cell.getColumnIndex()) {
                    case 0:
                        name = row.getCell(0).getStringCellValue();
                        break;
                    case 1:
                        info = row.getCell(1).getStringCellValue();
                        break;
                    case 2:
                        kcal = row.getCell(2).getNumericCellValue();
                        break;
                    case 3:
                        Optional<Unit> _unit = unitRepository.findById(row.getCell(3).getStringCellValue());
                        if (_unit.isPresent())
                            unit = _unit.get();
                        break;
                    default:
                        if (cell.getCellType().equals(CellType.NUMERIC)) {
                            double amount = cell.getNumericCellValue();
                            if (amount > 0) {
                                Optional<Nutrient> _nutrient = nutrientRepository.findById(ingredientSheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue());
                                if (_nutrient.isPresent())
                                    nutrientMap.put(_nutrient.get(), amount);
                            }
                        }
                        break;
                }
            Ingredient ingredient = Ingredient.builder().author(admin).name(name).info(info).kcal(kcal).unit(unit).build();
            ingredientRepository.save(ingredient);
            List<NutrientInfo> nutrientInfos = new ArrayList<>();
            for (Nutrient nutrient : nutrientMap.keySet()) {
                NutrientInfo nutrientInfo = NutrientInfo.builder().ingredient(ingredient).nutrient(nutrient).amount(nutrientMap.get(nutrient)).build();
                nutrientInfoRepository.save(nutrientInfo);
                nutrientInfos.add(nutrientInfo);
            }
            ingredient.setNutrientInfos(nutrientInfos);
            ingredientRepository.save(ingredient);
        }
        return list;
    }

    List<Recipe> initialRecipes(SiteUser admin, Sheet recipeSheet) {
        List<Recipe> list = new ArrayList<>();
        return list;
    }
}
