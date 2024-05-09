package com.StarJ.food_recipe.Global;

import com.StarJ.food_recipe.Entities.Categories.Category;
import com.StarJ.food_recipe.Entities.Categories.CategoryService;
import com.StarJ.food_recipe.Entities.Configs.ConfigService;
import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientService;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.Form.NutrientInfoForm;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfoService;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import com.StarJ.food_recipe.Entities.Nutrients.NutrientService;
import com.StarJ.food_recipe.Entities.PredictData.PredictDatumService;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.Form.IngredientInfoForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfoService;
import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEvalService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTagService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTools.RecipeToolService;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tags.TagService;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolService;
import com.StarJ.food_recipe.Entities.Units.UnitService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserService;
import com.StarJ.food_recipe.Global.Exceptions.DataNotFoundException;
import com.StarJ.food_recipe.Securities.UserRole;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class initialDataService {
    private final UserService userService;
    private final ToolService toolService;
    private final UnitService unitService;
    private final NutrientService nutrientService;
    private final RecipeToolService recipeToolService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final IngredientService ingredientService;
    private final NutrientInfoService nutrientInfoService;
    private final RecipeService recipeService;
    private final IngredientInfoService ingredientInfoService;
    private final RecipeTagService recipeTagService;
    private final RecipeEvalService recipeEvalService;
    private final ConfigService configService;
    private final PredictDatumService predictDatumService;
    private final ResourceLoader resourceLoader;

    @Async
    public void check() {
        List<SiteUser> users = userService.getUsers(UserRole.ADMIN.getValue());
        if (users.size() == 0)
            userService.create("admin", "1", "관리자", "", UserRole.ADMIN);
    }

    @Async
    public void initial() {
//        String filePath =  "C:/Users/admin/IdeaProjects/FoodRecipeWeb/initialData.xlsx";
        Resource resource = resourceLoader.getResource("classpath:/static/initialData.xlsx");
        try (InputStream inputStream = resource.getInputStream()) {
            System.out.println("데이터 입력 Start");
            ClassPathResource classPathResource = new ClassPathResource("initialData.xlsx");
            Workbook workbook = WorkbookFactory.create(inputStream);//(new File(filePath));

            initialUsers(workbook.getSheet("users"));
            SiteUser admin = userService.getUserbyID("admin");
            initialTools(admin, workbook.getSheet("tools"));
            initialUnits(admin, workbook.getSheet("units"));
            initialNutrients(admin, workbook.getSheet("nutrients"));
            initialCategories(admin, workbook.getSheet("categories"));
            initialTags(admin, workbook.getSheet("tags"));
            initialIngredients(admin, workbook.getSheet("ingredients"));

            initialRecipes(admin, workbook.getSheet("recipes"));
            configService.reset();
            workbook.close();

            // 평가
            initialEvals();
            System.out.println("데이터 입력 End");
        } catch (IOException ex) {
            System.out.print(ex.getLocalizedMessage());
        }
    }

    private void initialUsers(Sheet userSheet) {
        System.out.println("initializing Users Start");
        for (Row row : userSheet) {
            String id = row.getCell(0).getStringCellValue();
            String role = row.getCell(1).getStringCellValue();
            String nickname = row.getCell(2).getStringCellValue();
            String password = row.getCell(3).getCellType().equals(CellType.NUMERIC) ? String.format("%d", (int) row.getCell(3).getNumericCellValue()) : row.getCell(3).getStringCellValue();
            userService.create(id, password, nickname, id + "@mail.com", UserRole.getUserRole(role));
        }
        System.out.println("initializing Users End");
    }

    private void initialTools(SiteUser admin, Sheet toolSheet) {
        System.out.println("initializing Tools Start");
        for (Row row : toolSheet) {
            StringBuilder name = new StringBuilder();

            StringBuilder description = new StringBuilder();
            for (Cell cell : row)
                if (name.isEmpty()) name.append(cell.getStringCellValue());
                else {
                    if (!description.isEmpty()) description.append("\n");
                    description.append(cell.getStringCellValue());
                }
            toolService.create(admin, name.toString(), description.toString());
        }
        System.out.println("initializing Tools End");
    }

    private void initialUnits(SiteUser admin, Sheet unitSheet) {
        System.out.println("initializing Units Start");
        for (Row row : unitSheet) {
            StringBuilder name = new StringBuilder();
            StringBuilder description = new StringBuilder();
            for (Cell cell : row)
                if (name.isEmpty()) name.append(cell.getStringCellValue());
                else {
                    if (!description.isEmpty()) description.append("\n");
                    description.append(cell.getStringCellValue());
                }
            unitService.create(admin, name.toString(), description.toString());
        }
        System.out.println("initializing Units End");
    }

    private void initialNutrients(SiteUser admin, Sheet nutrientSheet) {
        System.out.println("initializing Nutrients Start");
        for (Row row : nutrientSheet) {
            StringBuilder name = new StringBuilder();
            StringBuilder description = new StringBuilder();
            for (Cell cell : row)
                if (name.isEmpty()) name.append(cell.getStringCellValue());
                else {
                    if (!description.isEmpty()) description.append("\n");
                    description.append(cell.getStringCellValue());
                }
            nutrientService.create(admin, name.toString(), description.toString());
        }
        System.out.println("initializing Nutrients End");
    }

    private void initialCategories(SiteUser admin, Sheet categorySheet) {
        System.out.println("initializing Categories Start");
        for (Row row : categorySheet)
            for (Cell cell : row)
                categoryService.create(admin, cell.getStringCellValue());
        System.out.println("initializing Categories End");
    }

    private void initialTags(SiteUser admin, Sheet tagSheet) {
        System.out.println("initializing Tag Start");
        for (Row row : tagSheet) {
            String name = row.getCell(0).getStringCellValue();
            String category_name = row.getCell(1).getStringCellValue();
            tagService.create(admin, name, category_name);
        }
        System.out.println("initializing Tag End");
    }

    private void initialIngredients(SiteUser admin, Sheet ingredientSheet) {
        System.out.println("initializing Ingredients Start");
        for (Row row : ingredientSheet) {
            if (row.getRowNum() == 0) continue;
            StringBuilder name = new StringBuilder();
            String info = null;
            double kcal = 0d;
            String unit = null;
            HashMap<String, Double> nutrientMap = new HashMap<>();
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
                        unit = row.getCell(3).getStringCellValue();
                        break;
                    default:
                        if (cell.getCellType().equals(CellType.NUMERIC)) {
                            double amount = cell.getNumericCellValue();
                            if (amount > 0) {
                                String nutrient = ingredientSheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
                                nutrientMap.put(nutrient, amount);
                            }
                        }
                        break;
                }
            List<NutrientInfoForm> list = new ArrayList<>();
            for (String nutrient : nutrientMap.keySet()) {
                NutrientInfoForm nutrientInfoForm = new NutrientInfoForm();
                nutrientInfoForm.setNutrient(nutrient);
                nutrientInfoForm.setAmount(nutrientMap.get(nutrient));
                list.add(nutrientInfoForm);
            }
            ingredientService.create(admin, name.toString(), info, kcal, unit, list);
        }
        System.out.println("initializing Ingredients End");
    }

    private void initialRecipes(SiteUser admin, Sheet recipeSheet) {
        System.out.println("initializing Recipes Start");
        for (Row row : recipeSheet) {
            StringBuilder subject = new StringBuilder();
            HashMap<String, Double> ingredientMap = new HashMap<>();
            List<String> tags = new ArrayList<>();
            for (Cell cell : row) {
                if (cell.getColumnIndex() == 0) subject.append(cell.getStringCellValue());
                else {
                    String origin = cell.getStringCellValue();
                    if (origin.contains(":")) {
                        String[] split = origin.split(":");
                        String ingredient = split[0];

                        if (ingredientService.has(ingredient)) {
                            double amount = 0d;
                            try {
                                amount = Double.parseDouble(split[1]);
                            } catch (Exception ex) {
                                System.out.println(ex.getLocalizedMessage());
                                System.out.print((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "은 잘못된 데이터 입니다.");
                            }
                            ingredientMap.put(ingredient, amount);
                        } else
                            throw new DataNotFoundException((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "는/은 없는 Ingredient 입니다.");
//                            System.out.println((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "는/은 없는 Ingredient 입니다.");
                    } else {
                        if (tagService.has(origin)) tags.add(origin);
                        else
                            throw new DataNotFoundException((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "은 없는 Tag 입니다.");
                    }
                }
            }
            List<IngredientInfoForm> ingredientInfos = new ArrayList<>();
            for (String ingredient : ingredientMap.keySet()) {
                IngredientInfoForm ingredientInfoForm = new IngredientInfoForm();
                ingredientInfoForm.setIngredient(ingredient);
                ingredientInfoForm.setAmount(ingredientMap.get(ingredient));
                ingredientInfos.add(ingredientInfoForm);
            }
            recipeService.create(admin, subject.toString(), "", tags, new ArrayList<>(), new ArrayList<>(), ingredientInfos);
        }
        System.out.println("initializing Recipes End");
    }

    public String getColumn(int column_index) {
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

    private void initialEvals() {
        System.out.println("initializing Evals start");
        List<SiteUser> users = userService.getUsers(UserRole.USER.getValue());
        List<Recipe> recipes = recipeService.getRecipes();
        Random r = new Random();
        for (SiteUser user : users) {
            Collections.shuffle(recipes);
            for (int i = 0; i < Math.max(r.nextInt(recipes.size()), recipes.size() * 3 / 4); i++)
                recipeEvalService.setEval(user, recipes.get(i), Math.ceil(r.nextDouble() * 100d / 2d) / 10d);
        }
        System.out.println("initializing Evals End");
    }

    @Async
    public void resetAllData() {
        System.out.println("reset start");
        configService.reset();
        System.out.println("config reset");
        recipeEvalService.reset();
        System.out.println("eval reset");
        recipeTagService.reset();
        System.out.println("recipe_tag reset");
        ingredientInfoService.reset();
        System.out.println("ing_info reset");
        nutrientInfoService.reset();
        System.out.println("nut_info reset");
        ingredientService.reset();
        System.out.println("ing reset");
        tagService.reset();
        System.out.println("tag reset");
        categoryService.reset();
        System.out.println("cat reset");
        nutrientService.reset();
        System.out.println("nut reset");
        unitService.reset();
        System.out.println("unit reset");
        recipeToolService.reset();
        System.out.println("recipe_tool reset");
        toolService.reset();
        System.out.println("tool reset");
        predictDatumService.reset();
        System.out.println("pd reset");
        recipeService.reset();
        System.out.println("recipe reset");
        userService.reset();
        System.out.println("user reset");
        userService.create("admin", "1", "관리자", "", UserRole.ADMIN);
        System.out.println("reset finished");
    }

    @Async
    public void createTestData() {
        // 유저 1만명
        // Recipe 10만개
        // 평가 100만개
        // 사용자
        System.out.println("User Create Start");
        SiteUser admin = userService.create("admin", "1", "관리자", "", UserRole.ADMIN);
        for (int i = 1; i <= 1000; i++) {
            userService.create("user" + i, "", "", "", UserRole.USER);
            if (i % 1000 == 0)
                System.out.println("User Create Percentage : " + Math.round(i / 100d) + "%");
        }
        System.out.println("User Create End");
        // Tool
        System.out.println("Tool Create Start");
        for (int i = 1; i <= 200; i++)
            toolService.create(admin, "Tool" + i, i + "번째로 Create된 Tool");
        System.out.println("Tool Create End");
        // Unit
        System.out.println("Unit Create Start");
        for (int i = 1; i <= 100; i++)
            unitService.create(admin, "Unit" + i, i + "번째로 Create된 Unit");
        System.out.println("Unit Create End");
        // Nutrient
        System.out.println("Nutrient Create Start");
        for (int i = 1; i <= 100; i++)
            nutrientService.create(admin, "Nutrient" + i, i + "번째로 Create된 Nutrient");
        System.out.println("Nutrient Create End");
        // Category
        System.out.println("Category Create Start");
        for (int i = 1; i <= 10; i++)
            categoryService.create(admin, "Category" + i);
        System.out.println("Category Create End");
        // Tag
        System.out.println("Tag Create Start");
        List<Category> categories = categoryService.getCategories();
        for (int i = 1; i <= 100; i++) {
            if (i % 10 == 0)
                Collections.shuffle(categories);
            tagService.create(admin, "Tag" + i, categories.get(i % 10).getName());
        }
        System.out.println("Tag Create End");
        // Ingredient
        System.out.println("Ingredient Create Start");
        List<Nutrient> nutrients = nutrientService.getNutrients();
        for (int i = 1; i <= 1000; i++) {
            List<NutrientInfoForm> nutrientInfoForms = new ArrayList<>();
            Collections.shuffle(nutrients);
            for (int k = 0; k < 5 + new Random().nextInt(16); k++) {
                NutrientInfoForm form = new NutrientInfoForm();
                form.setNutrient(nutrients.get(k).getName());
                form.setAmount(5);
                nutrientInfoForms.add(form);
            }
            ingredientService.create(admin, "Ingredient" + i, i + "번째로 Create된 Ingredient", 100, "", nutrientInfoForms);
        }
        System.out.println("Ingredient Create End");
        // Recipe
        System.out.println("Recipe Create Start");
        List<Tag> tags = tagService.getTags();
        List<Tool> tools = toolService.getTools();
        List<Ingredient> ingredients = ingredientService.getIngredients();
        for (int i = 1; i <= 3000; i++) {
            List<String> _tags = new ArrayList<>();
            if (i % (tags.size() / 5) == 0)
                Collections.shuffle(tags);
            for (int k = 0; k < 5; k++)
                _tags.add(tags.get(k).getName());
            List<String> _tools = new ArrayList<>();
            if (i % (tools.size() / 5) == 0)
                Collections.shuffle(tools);
            for (int k = 0; k < 5; k++)
                _tools.add(tools.get(k).getName());
            List<IngredientInfoForm> ingredientInfoForms = new ArrayList<>();
            if (i % (ingredients.size() / 10) == 0)
                Collections.shuffle(ingredients);
            for (int k = 0; k < 10; k++) {
                IngredientInfoForm ingredientInfoForm = new IngredientInfoForm();
                ingredientInfoForm.setIngredient(ingredients.get(k).getName());
                ingredientInfoForm.setAmount(5);
                ingredientInfoForms.add(ingredientInfoForm);
            }
            if (i % 1000 == 0)
                System.out.println("recipe Create Percentage : " + (i / 1000d) + "%");
            recipeService.create(admin, i + "번째 만드는 Recipe", "", _tags, _tools, new ArrayList<>(), ingredientInfoForms);
        }
        System.out.println("Recipe Create End");
        System.out.println("Eval Create Start");
        List<Recipe> recipes = recipeService.getRecipes();
        int i=0;
        for (SiteUser user : userService.getUsers(UserRole.USER.getValue())) {
            Collections.shuffle(recipes);
            for (int k = 0; k < 100; k++) {
                Recipe recipe = recipes.get(k);
                recipeEvalService.setEval(user, recipe, Math.ceil(new Random().nextDouble() * 50) / 10d);
            }
            i++;
            if(i%100==0)
                System.out.println("Eval Create Percentage : "+(i/100)+"%");
        }

        System.out.println("Eval Create End");


    }
}