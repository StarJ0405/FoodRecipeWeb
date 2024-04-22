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
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEval;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEvalRepository;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private RecipeEvalRepository recipeEvalRepository;

    //&#10;
    @Test
//    @Transactional
    void initialData() {
        try {
            String filePath = "C:/Users/admin/IdeaProjects/FoodRecipeWeb/initialData.xlsx";
            Workbook workbook = WorkbookFactory.create(new File(filePath));

            List<SiteUser> users = initialUsers(workbook.getSheet("users"));
            SiteUser admin = null;
            for (int i = 0; i < users.size(); i++) {
                SiteUser user = users.get(i);
                if (user.getRole().equals(UserRole.ADMIN.getValue())) {
                    admin = user;
                    users.remove(i);
                    break;
                }
            }
            initialTools(admin, workbook.getSheet("tools"));
            initialUnits(admin, workbook.getSheet("units"));
            initialNutrients(admin, workbook.getSheet("nutrients"));
            initialCategories(admin, workbook.getSheet("categories"));
            initialTags(admin, workbook.getSheet("tags"));
            initialIngredients(admin, workbook.getSheet("ingredients"));

            initialRecipes(admin, workbook.getSheet("recipes"));
            workbook.close();

            // 평가
            initialEval();

            // 저장
            String definePath = "C:/Users/admin/IdeaProjects/FoodRecipeWeb/defined.xlsx";
            FileInputStream in = new FileInputStream(definePath);
            Workbook defined_workbook = WorkbookFactory.create(in);
            Sheet sheet = defined_workbook.getSheetAt(0);
            // 데이터 초기화
            for (Row row : sheet)
                for (Cell cell : row)
                    cell.setCellValue("");
            // 데이터 입력
            int i = 0;
            for (Recipe recipe : recipeRepository.findAll())
                for (SiteUser user : users) {
                    Optional<RecipeEval> _eval = recipeEvalRepository.findByUserRecipe(user, recipe);
                    if (_eval.isPresent()) {
                        Row row = sheet.createRow(i);
                        row.createCell(0).setCellValue(user.getId());
                        row.createCell(1).setCellValue(recipe.getId());
                        row.createCell(2).setCellValue(_eval.get().getVal());
                        row.createCell(3).setCellValue(Long.parseLong(_eval.get().getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));
                        i++;
                    }
                }

            // 반환
            FileOutputStream out = new FileOutputStream(definePath);
            defined_workbook.write(out);
            out.close();
            defined_workbook.close();
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

    void initialTools(SiteUser admin, Sheet toolSheet) {
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
        }
    }

    void initialUnits(SiteUser admin, Sheet unitSheet) {
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
        }
    }

    void initialNutrients(SiteUser admin, Sheet nutrientSheet) {
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
        }
    }

    void initialCategories(SiteUser admin, Sheet categorySheet) {
        for (Row row : categorySheet)
            for (Cell cell : row) {
                String name = cell.getStringCellValue();
                Optional<Category> _category = categoryRepository.findById(name);
                Category category = _category.orElseGet(() -> Category.builder().author(admin).name(name).build());
                categoryRepository.save(category);
            }
    }

    void initialTags(SiteUser admin, Sheet tagSheet) {
        for (Row row : tagSheet) {
            String name = row.getCell(0).getStringCellValue();
            Optional<Tag> _tag = tagRepository.findById(name);
            String category_name = row.getCell(1).getStringCellValue();
            Optional<Category> _category = categoryRepository.findById(category_name);

            Tag tag = _tag.orElseGet(() -> Tag.builder().author(admin).name(name).build());
            tag.setCategory(_category.orElse(null));
            tagRepository.save(tag);
        }
    }

    @Transactional
    void initialIngredients(SiteUser admin, Sheet ingredientSheet) {
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
                                _nutrient.ifPresent(nutrient -> nutrientMap.put(nutrient, amount));
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
                nutrientInfos.add(nutrientInfo);
            }
            ingredient.setNutrientInfos(nutrientInfos);
            ingredientRepository.save(ingredient);
        }
    }

    @Transactional
    void initialRecipes(SiteUser admin, Sheet recipeSheet) {
        for (Row row : recipeSheet) {
            StringBuilder subject = new StringBuilder();
            HashMap<Ingredient, Double> ingredientMap = new HashMap<>();
            List<Tag> tags = new ArrayList<>();
            Iterator<Cell> iterator = row.iterator();
            while (iterator.hasNext()) {
                Cell cell = iterator.next();
                if (cell.getColumnIndex() == 0)
                    subject.append(cell.getStringCellValue());
                else {
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
                            throw new DataNotFoundException((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "는/은 없는 재료 입니다.");
//                            System.out.println((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "는/은 없는 재료 입니다.");
                    } else {
                        Optional<Tag> _tag = tagRepository.findById(origin);
                        if (_tag.isPresent()) {
                            tags.add(_tag.get());
                        } else
                            throw new DataNotFoundException((row.getRowNum() + 1) + "," + getColumn(cell.getColumnIndex()) + " - " + origin + "은 없는 태그 입니다.");
                    }
                }
                if(row.getRowNum()==5)
                System.out.println(cell.getCellType().name()+" - "+(cell.getCellType().equals(CellType.NUMERIC)?cell.getNumericCellValue():cell.getStringCellValue()));
            }
            Optional<Recipe> _recipe = recipeRepository.search(subject.toString());
            Recipe recipe = _recipe.orElseGet(() -> Recipe.builder().author(admin).uuid(UUID.randomUUID()).subject(subject.toString()).build());
            recipeRepository.save(recipe);
            List<IngredientInfo> ingredientInfos = new ArrayList<>();
            for (Ingredient ingredient : ingredientMap.keySet()) {
                IngredientInfo ingredientInfo = IngredientInfo.builder().recipe(recipe).ingredient(ingredient).amount(ingredientMap.get(ingredient)).build();
                ingredientInfos.add(ingredientInfo);
            }
            List<RecipeTag> recipeTags = new ArrayList<>();
            for (Tag tag : tags) {
                RecipeTag recipeTag = RecipeTag.builder().recipe(recipe).tag(tag).build();
                recipeTags.add(recipeTag);
            }
            recipe.setIngredientInfos(ingredientInfos);
            recipe.setTags(recipeTags);
            recipe.setTools(new ArrayList<>());
            recipeRepository.save(recipe);
        }
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

    @Transactional
    public void initialEval() {
        List<SiteUser> users = userRepository.findAll();
        List<Recipe> recipes = recipeRepository.findAll();
        Random r = new Random();

        HashMap<Recipe, List<RecipeEval>> evalMap = new HashMap<>();
        for (SiteUser user : users) {
            Collections.shuffle(recipes);
            for (int i = 0; i < Math.max(r.nextInt(recipes.size()), recipes.size() * 3 / 4); i++) {
                Recipe recipe = recipes.get(i);
                if (!evalMap.containsKey(recipe))
                    evalMap.put(recipe, new ArrayList<>());
                List<RecipeEval> evals = evalMap.get(recipe);
                Optional<RecipeEval> _recipeEval = recipeEvalRepository.findByUserRecipe(user, recipe);
                RecipeEval recipeEval = _recipeEval.orElseGet(() -> RecipeEval.builder().siteUser(user).recipe(recipe).build());
                recipeEval.setVal(Math.ceil(r.nextDouble() * 100d / 2d) / 10d);

                evals.add(recipeEval);
                evalMap.put(recipe, evals);
            }
        }

        for (Recipe recipe : evalMap.keySet()) {
            List<RecipeEval> recipeEvals = recipe.getEvals();
            recipeEvals.clear();
            recipeEvals.addAll(evalMap.get(recipe));
            recipeRepository.save(recipe);
        }
    }

}
