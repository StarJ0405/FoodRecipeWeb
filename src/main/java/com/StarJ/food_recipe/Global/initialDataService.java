package com.StarJ.food_recipe.Global;

import com.StarJ.food_recipe.Entities.Categories.CategoryService;
import com.StarJ.food_recipe.Entities.Configs.ConfigService;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientService;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.Form.NutrientInfoForm;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfoService;
import com.StarJ.food_recipe.Entities.Nutrients.NutrientService;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.Form.IngredientInfoForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfoService;
import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEvalService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTagService;
import com.StarJ.food_recipe.Entities.Tags.TagService;
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
    private final CategoryService categoryService;
    private final TagService tagService;
    private final IngredientService ingredientService;
    private final NutrientInfoService nutrientInfoService;
    private final RecipeService recipeService;
    private final IngredientInfoService ingredientInfoService;
    private final RecipeTagService recipeTagService;
    private final RecipeEvalService recipeEvalService;
    private final ConfigService configService;
    private final ResourceLoader resourceLoader;
    @Async
    public void check() {
        List<SiteUser> users = userService.getUsers(UserRole.ADMIN.getValue());
        if (users.size() == 0)
            initial();
    }

    @Async
    public void initial() {
//        String filePath =  "C:/Users/admin/IdeaProjects/FoodRecipeWeb/initialData.xlsx";
        Resource resource = resourceLoader.getResource("classpath:/static/initialData.xlsx");
        try (InputStream inputStream = resource.getInputStream()) {

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
            initialEval();
        } catch (IOException ex) {
            System.out.print(ex.getLocalizedMessage());
        }
    }

    private void initialUsers(Sheet userSheet) {
        for (Row row : userSheet) {
            String id = row.getCell(0).getStringCellValue();
            String role = row.getCell(1).getStringCellValue();
            String nickname = row.getCell(2).getStringCellValue();
            String password = row.getCell(3).getCellType().equals(CellType.NUMERIC) ? String.format("%d", (int) row.getCell(3).getNumericCellValue()) : row.getCell(3).getStringCellValue();
            userService.create(id, password, nickname, id + "@mail.com", UserRole.getUserRole(role));
        }
    }

    private void initialTools(SiteUser admin, Sheet toolSheet) {
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
    }

    private void initialUnits(SiteUser admin, Sheet unitSheet) {
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
    }

    private void initialNutrients(SiteUser admin, Sheet nutrientSheet) {
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
    }

    private void initialCategories(SiteUser admin, Sheet categorySheet) {
        for (Row row : categorySheet)
            for (Cell cell : row) {
                categoryService.create(admin, cell.getStringCellValue());
                return;
            }
    }

    private void initialTags(SiteUser admin, Sheet tagSheet) {
        for (Row row : tagSheet) {
            String name = row.getCell(0).getStringCellValue();
            String category_name = row.getCell(1).getStringCellValue();
            tagService.create(admin, name, category_name);
        }
    }

    private void initialIngredients(SiteUser admin, Sheet ingredientSheet) {
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
    }

    private void initialRecipes(SiteUser admin, Sheet recipeSheet) {
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
                            throw new DataNotFoundException((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "는/은 없는 재료 입니다.");
//                            System.out.println((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "는/은 없는 재료 입니다.");
                    } else {
                        if (tagService.has(origin)) tags.add(origin);
                        else
                            throw new DataNotFoundException((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "은 없는 태그 입니다.");
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

    private void initialEval() {
        List<SiteUser> users = userService.getUsers(UserRole.USER.getValue());
        List<Recipe> recipes = recipeService.getRecipes();
        Random r = new Random();
        for (SiteUser user : users) {
            Collections.shuffle(recipes);
            for (int i = 0; i < Math.max(r.nextInt(recipes.size()), recipes.size() * 3 / 4); i++)
                recipeEvalService.setEval(user, recipes.get(i), Math.ceil(r.nextDouble() * 100d / 2d) / 10d);
        }
    }
}
