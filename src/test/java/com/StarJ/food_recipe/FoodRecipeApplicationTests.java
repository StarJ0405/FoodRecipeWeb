package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Entities.Categories.Category;
import com.StarJ.food_recipe.Entities.Categories.CategoryRepository;
import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientRepository;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfoRepository;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import com.StarJ.food_recipe.Entities.Nutrients.NutrientRepository;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfoRepository;
import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Recipes.RecipeRepository;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTag;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTagRepository;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tags.TagRepository;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolRepository;
import com.StarJ.food_recipe.Entities.Units.Unit;
import com.StarJ.food_recipe.Entities.Units.UnitRepository;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserRepository;
import com.StarJ.food_recipe.Global.Exceptions.DataNotFoundException;
import com.StarJ.food_recipe.Securities.UserRole;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientInfoRepository ingredientInfoRepository;
    @Autowired
    private RecipeTagRepository recipeTagRepository;

    //&#10;
    @Test
//    @Transactional
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
            List<Recipe> recipes = initialRecipes(admin, workbook.getSheet("recipes"));
            workbook.close();
        } catch (IOException ex) {
            System.out.print(ex.getLocalizedMessage());
        }
    }

    List<SiteUser> initialUsers(Sheet userSheet) {
        List<SiteUser> list = new ArrayList<>();
        for (Row row : userSheet) {
            String id = row.getCell(0).getStringCellValue();
            String role = row.getCell(1).getStringCellValue();
            String nickname = row.getCell(2).getStringCellValue();
            String password = row.getCell(3).getCellType().equals(CellType.NUMERIC) ? String.format("%d", (int) row.getCell(3).getNumericCellValue()) : row.getCell(3).getStringCellValue();
            SiteUser user = SiteUser.builder().id(id).role(role).nickname(nickname).password(passwordEncoder.encode(password)).build();
            userRepository.save(user);
            list.add(user);
        }
        return list;
    }

    List<Tool> initialTools(SiteUser admin, Sheet toolSheet) {
        List<Tool> list = new ArrayList<>();
        for (Row row : toolSheet) {
            StringBuilder name = new StringBuilder();

            StringBuilder description = new StringBuilder();
            for (Cell cell : row)
                if (name.isEmpty())
                    name.append(cell.getStringCellValue());
                else {
                    if (!description.isEmpty())
                        description.append("\n");
                    description.append(cell.getStringCellValue());
                }
            Optional<Tool> _tool = toolRepository.findById(name.toString());
            Tool tool = _tool.orElseGet(() -> Tool.builder().author(admin).name(name.toString()).build());
            tool.setDescription(description.toString());
            toolRepository.save(tool);
            list.add(tool);
        }
        return list;
    }

    List<Unit> initialUnits(SiteUser admin, Sheet unitSheet) {
        List<Unit> list = new ArrayList<>();
        for (Row row : unitSheet) {
            StringBuilder name = new StringBuilder();
            StringBuilder description = new StringBuilder();
            for (Cell cell : row)
                if (name.isEmpty())
                    name.append(cell.getStringCellValue());
                else {
                    if (!description.isEmpty())
                        description.append("\n");
                    description.append(cell.getStringCellValue());
                }
            Optional<Unit> _unit = unitRepository.findById(name.toString());
            Unit unit = _unit.orElseGet(() -> Unit.builder().author(admin).name(name.toString()).build());
            unit.setDescription(description.toString());
            unitRepository.save(unit);
            list.add(unit);
        }
        return list;
    }

    List<Nutrient> initialNutrients(SiteUser admin, Sheet nutrientSheet) {
        List<Nutrient> list = new ArrayList<>();
        for (Row row : nutrientSheet) {
            StringBuilder name = new StringBuilder();
            StringBuilder description = new StringBuilder();
            for (Cell cell : row)
                if (name.isEmpty())
                    name.append(cell.getStringCellValue());
                else {
                    if (!description.isEmpty())
                        description.append("\n");
                    description.append(cell.getStringCellValue());
                }
            Optional<Nutrient> _nutrient = nutrientRepository.findById(name.toString());
            Nutrient nutrient = _nutrient.orElseGet(() -> Nutrient.builder().author(admin).name(name.toString()).build());
            nutrient.setDescription(description.toString());
            nutrientRepository.save(nutrient);
            list.add(nutrient);
        }
        return list;
    }

    List<Category> initialCategories(SiteUser admin, Sheet categorySheet) {
        List<Category> list = new ArrayList<>();
        for (Row row : categorySheet)
            for (Cell cell : row) {
                String name = cell.getStringCellValue();
                Optional<Category> _category = categoryRepository.findById(name);
                Category category = _category.orElseGet(() -> Category.builder().author(admin).name(name).build());
                categoryRepository.save(category);
                list.add(category);
            }
        return list;
    }

    List<Tag> initialTags(SiteUser admin, Sheet tagSheet) {
        List<Tag> list = new ArrayList<>();
        for (Row row : tagSheet) {
            String name = row.getCell(0).getStringCellValue();
            Optional<Tag> _tag = tagRepository.findById(name);
            String category_name = row.getCell(1).getStringCellValue();
            Optional<Category> _category = categoryRepository.findById(category_name);

            Tag tag = _tag.orElseGet(() -> Tag.builder().author(admin).name(name).build());
            tag.setCategory(_category.orElse(null));
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
            StringBuilder name = new StringBuilder();
            String info = null;
            double kcal = 0d;
            Unit unit = null;
            HashMap<Nutrient, Double> nutrientMap = new HashMap<>();
            for (Cell cell : row)
                switch (cell.getColumnIndex()) {
                    case 0:
                        name.append(row.getCell(0).getStringCellValue());
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
                                _nutrient.ifPresent(nutrient -> nutrientMap.put(nutrient, amount / 100d));
                            }
                        }
                        break;
                }
            Optional<Ingredient> _ingredient = ingredientRepository.findById(name.toString());
            Ingredient ingredient = _ingredient.orElseGet(() -> Ingredient.builder().author(admin).name(name.toString()).build());
            ingredient.setInfo(info);
            ingredient.setKcal(kcal);
            ingredient.setUnit(unit);
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

    @Transactional
    List<Recipe> initialRecipes(SiteUser admin, Sheet recipeSheet) {
        List<Recipe> list = new ArrayList<>();
        for (Row row : recipeSheet) {
            StringBuilder subject = new StringBuilder();
            boolean ing = true;
            HashMap<Ingredient, Double> ingredientMap = new HashMap<>();
            List<Tag> tags = new ArrayList<>();
            for (Cell cell : row)
                if (cell.getColumnIndex() == 0)
                    subject.append(cell.getStringCellValue());
                else if (cell.getCellType().equals(CellType.BLANK))
                    ing = false;
                else if (ing) {
                    String origin = cell.getStringCellValue();
                    if (origin.contains(":")) {
                        String[] split = origin.split(":");
                        Optional<Ingredient> _ingredient = ingredientRepository.findById(split[0]);

                        if (_ingredient.isPresent()) {
                            Ingredient ingredient = _ingredient.get();
                            double amount = 0d;
                            try {
                                amount = Double.parseDouble(split[1]);
                            } catch (Exception ex) {
                                System.out.println(ex.getLocalizedMessage());
                                System.out.print((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "은 잘못된 데이터 입니다.");
                            }
                            ingredientMap.put(ingredient, amount);
                        } else
                            throw new DataNotFoundException((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "은 잘못된 데이터 입니다.");
                    }
                } else {
                    String origin = cell.getStringCellValue();
                    Optional<Tag> _tag = tagRepository.findById(origin);
                    if (_tag.isPresent()) {
                        tags.add(_tag.get());
                    } else
                        throw new DataNotFoundException((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "은 잘못된 데이터 입니다.");
                }
            Optional<Recipe> _recipe = recipeRepository.search(subject.toString());
            Recipe recipe = _recipe.orElseGet(() -> Recipe.builder().author(admin).uuid(UUID.randomUUID()).subject(subject.toString()).build());
            recipeRepository.save(recipe);
            List<IngredientInfo> ingredientInfos = new ArrayList<>();
            for (Ingredient ingredient : ingredientMap.keySet()) {
                IngredientInfo ingredientInfo = IngredientInfo.builder().recipe(recipe).ingredient(ingredient).amount(ingredientMap.get(ingredient)).build();
                ingredientInfoRepository.save(ingredientInfo);
                ingredientInfos.add(ingredientInfo);
            }
            List<RecipeTag> recipeTags = new ArrayList<>();
            for (Tag tag : tags) {
                RecipeTag recipeTag = RecipeTag.builder().recipe(recipe).tag(tag).build();
                recipeTagRepository.save(recipeTag);
                recipeTags.add(recipeTag);
            }
            recipe.setIngredientInfos(ingredientInfos);
            recipe.setTags(recipeTags);
            recipe.setTools(new ArrayList<>());
            recipeRepository.save(recipe);
            list.add(recipe);
        }
        return list;
    }

    private static String getColumn(int column_index) {
        List<Character> list = new ArrayList<>();
        do {
            list.add((char) ('A' + (column_index % 26)));
            column_index = column_index / 26 - 1;
        } while (column_index >= 0);
        Collections.reverse(list);
        StringBuilder str = new StringBuilder();
        for (char c : list)
            str.append(c);
        return str.toString();
    }
}
