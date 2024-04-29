package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientService;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImage;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImageService;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.Form.BodyImageForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.Form.IngredientInfoForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfoService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEval;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTag;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTagService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTools.RecipeTool;
import com.StarJ.food_recipe.Entities.Recipes.RecipeTools.RecipeToolService;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tags.TagService;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.FoodRecipeApplication;
import com.StarJ.food_recipe.Global.Exceptions.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final TagService tagService;
    private final ToolService toolService;
    private final BodyImageService bodyImageService;
    private final IngredientService ingredientService;
    private final IngredientInfoService ingredientInfoService;
    private final RecipeToolService recipeToolService;
    private final RecipeTagService recipeTagService;

    public List<Recipe> getUnseenRecipe(SiteUser user) {
        return recipeRepository.unseenSearch(user);
    }

    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    public Page<Recipe> getRecipes(int page, String kw, String[] tags) {
        Pageable pageable = PageRequest.of(page, 24);
        return tags != null ? recipeRepository.recipePage(pageable, kw, Arrays.asList(tags)) : recipeRepository.recipePage(pageable, kw);
    }

    public Recipe getRecipe(Integer id) {
        Optional<Recipe> _recipe = recipeRepository.findById(id);
        if (_recipe.isPresent())
            return _recipe.get();
        else
            throw new DataNotFoundException("없는 데이터입니다.");
    }

    public void modify(Recipe recipe, SiteUser user, String subject, String baseImg, List<String> _tags, List<String> _tools, List<BodyImageForm> bodyImageForms, List<IngredientInfoForm> ingredientInfoForms) {
        recipe.setModifier(user);
        recipe.setModifiedDate(LocalDateTime.now());
        recipe.setSubject(subject);
        recipe.setBaseImg(baseImg);
        List<RecipeTool> recipeTools = new ArrayList<>();
        for (Tool tool : toolService.getTools(_tools))
            recipeTools.add(recipeToolService.getRecipeTool(recipe, tool));
        recipe.setTools(recipeTools);
        List<RecipeTag> recipeTags = new ArrayList<>();
        for (Tag tag : tagService.getTags(_tags))
            recipeTags.add(recipeTagService.getRecipeTag(recipe, tag));
        recipe.setTags(recipeTags);

        List<BodyImage> bodyImages = recipe.getBodyImages();
        bodyImages.clear();
        for (BodyImageForm form : bodyImageForms) {
            BodyImage bodyImage = bodyImageService.getBodyImage(recipe, form.getBody(), form.getImgURL());
            if (bodyImage != null)
                bodyImages.add(bodyImage);
        }
        List<IngredientInfo> ingredientInfos = recipe.getIngredientInfos();
        ingredientInfos.clear();
        for (IngredientInfoForm form : ingredientInfoForms) {
            Ingredient ingredient = ingredientService.getIngredient(form.getIngredient());
            IngredientInfo ingredientInfo = ingredientInfoService.getModifiedIngredientInfo(recipe, ingredient, form.getAmount());
            if (ingredientInfo != null)
                ingredientInfos.add(ingredientInfo);
        }
        recipeRepository.save(recipe);
    }

    public boolean has(int id) {
        return recipeRepository.findById(id).isPresent();
    }

    public void delete(Recipe recipe) {
        String path = FoodRecipeApplication.getOS_TYPE().getPath();
        File recipeFolder = new File(path + "/recipes/" + recipe.getUUID().toString());
        deleteFile(recipeFolder);
        recipeRepository.delete(recipe);
    }

    private void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files)
                    if (f.isDirectory())
                        deleteFile(f);
                    else
                        f.delete();
                file.delete();
            } else file.delete();
        } else System.out.println("not exists");
    }

    public void edit(Recipe recipe, SiteUser user, String subject, String baseImg, List<String> _tags, List<String> _tools, List<BodyImageForm> bodyImageForms, List<IngredientInfoForm> ingredientInfoForms) {
        recipe.setModifier(user);
        recipe.setModifiedDate(LocalDateTime.now());
        recipe.setSubject(subject);
        if (baseImg != null && !baseImg.equals("") && !baseImg.equals(recipe.getBaseImg()))
            try {
                String path = FoodRecipeApplication.getOS_TYPE().getPath();
                Path oldPath = Paths.get(path + baseImg);
                File recipeFolder = new File(path + "/recipes/" + recipe.getUUID().toString());
                if (!recipeFolder.exists())
                    recipeFolder.mkdirs();
                String newUrl = "/recipes/" + recipe.getUUID().toString() + "/baseImg." + baseImg.split("\\.")[1];
                Path newPath = Paths.get(path + newUrl);
                Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                recipe.setBaseImg(newUrl);
            } catch (IOException ex) {

            }
        List<BodyImage> bodyImages = recipe.getBodyImages();
        bodyImages.clear();
        for (int i = 0; i < bodyImageForms.size(); i++) {
            BodyImageForm form = bodyImageForms.get(i);
            BodyImage bodyImage = bodyImageService.getBodyImage(recipe, form.getBody(), form.getImgURL());
            String imgURL = bodyImage.getImgURL();
            if (imgURL != null && !imgURL.equals(""))
                try {
                    String path = FoodRecipeApplication.getOS_TYPE().getPath();
                    Path oldPath = Paths.get(path + imgURL);
                    File recipeFolder = new File(path + "/recipes/" + recipe.getUUID().toString());
                    if (!recipeFolder.exists())
                        recipeFolder.mkdirs();
                    String newUrl = "/recipes/" + recipe.getUUID().toString() + "/bodyImg_" + i + "." + imgURL.split("\\.")[1];
                    Path newPath = Paths.get(path + newUrl);
                    Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                    bodyImage.setImgURL(newUrl);
                } catch (IOException ex) {

                }
            bodyImages.add(bodyImage);
        }
        List<IngredientInfo> ingredientInfos = recipe.getIngredientInfos();
        ingredientInfos.clear();
        for (IngredientInfoForm form : ingredientInfoForms) {
            Ingredient ingredient = ingredientService.getIngredient(form.getIngredient());
            IngredientInfo ingredientInfo = ingredientInfoService.getIngredientInfo(recipe, ingredient, form.getAmount());
            if (ingredientInfo != null)
                ingredientInfos.add(ingredientInfo);
        }
        List<RecipeTool> tools = recipe.getTools();
        tools.clear();
        for (Tool tool : toolService.getTools(_tools))
            tools.add(recipeToolService.getRecipeTool(recipe, tool));
        recipe.setTools(tools);
        List<RecipeTag> tags = recipe.getTags();
        tags.clear();
        for (Tag tag : tagService.getTags(_tags))
            tags.add(recipeTagService.getRecipeTag(recipe, tag));
        recipe.setTags(tags);
        recipeRepository.save(recipe);
    }

    @Transactional
    public void create(SiteUser user, String subject, String baseImg, List<String> _tags, List<String> _tools, List<BodyImageForm> bodyImageForms, List<IngredientInfoForm> ingredientInfoForms) {
        Recipe recipe = Recipe.builder().author(user).subject(subject).uuid(UUID.randomUUID()).build();
        if (baseImg != null && !baseImg.equals(""))
            try {
                String path = FoodRecipeApplication.getOS_TYPE().getPath();
                Path oldPath = Paths.get(path + baseImg);
                File recipeFolder = new File(path + "/recipes/" + recipe.getUUID().toString());
                if (!recipeFolder.exists())
                    recipeFolder.mkdirs();
                String newUrl = "/recipes/" + recipe.getUUID().toString() + "/baseImg." + baseImg.split("\\.")[1];
                Path newPath = Paths.get(path + newUrl);
                Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                recipe.setBaseImg(newUrl);
            } catch (IOException ex) {

            }
        List<BodyImage> bodyImages = recipe.getBodyImages();
        for (BodyImageForm form : bodyImageForms) {
            BodyImage bodyImage = bodyImageService.getBodyImage(recipe, form.getBody(), form.getImgURL());
            bodyImages.add(bodyImage);
        }
        List<IngredientInfo> ingredientInfos = recipe.getIngredientInfos();
        for (IngredientInfoForm form : ingredientInfoForms) {
            Ingredient ingredient = ingredientService.getIngredient(form.getIngredient());
            IngredientInfo ingredientInfo = ingredientInfoService.getIngredientInfo(recipe, ingredient, form.getAmount());
            if (ingredientInfo != null)
                ingredientInfos.add(ingredientInfo);
        }
        List<RecipeTool> tools = new ArrayList<>();
        for (Tool tool : toolService.getTools(_tools))
            tools.add(recipeToolService.getRecipeTool(recipe, tool));
        recipe.setTools(tools);

        List<RecipeTag> tags = new ArrayList<>();
        for (Tag tag : tagService.getTags(_tags))
            tags.add(recipeTagService.getRecipeTag(recipe, tag));
        recipe.setTags(tags);
        recipeRepository.save(recipe);
    }

    public String saveTempImage(MultipartFile file, SiteUser user) {
        if (!file.isEmpty())
            try {
                String path = FoodRecipeApplication.getOS_TYPE().getPath();
                File userFolder = new File(path + "/users/" + user.getId());
                if (!userFolder.exists())
                    userFolder.mkdirs();
                String fileloc = "/users/" + user.getId() + "/temp_recipe." + file.getContentType().split("/")[1];
                file.transferTo(new File(path + fileloc));
                return fileloc;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        return null;
    }

    public String saveTempBodyImage(MultipartFile file, SiteUser user) {
        if (!file.isEmpty())
            try {
                String path = FoodRecipeApplication.getOS_TYPE().getPath();
                File userFolder = new File(path + "/users/" + user.getId());
                if (!userFolder.exists())
                    userFolder.mkdirs();
                String fileloc = "/users/" + user.getId() + "/temp_recipe_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh_mm_ss_SSSS")) + "." + file.getContentType().split("/")[1];
                file.transferTo(new File(path + fileloc));
                return fileloc;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        return null;
    }

    public void removeRecipeEval(RecipeEval recipeEval) {
        Recipe recipe = recipeEval.getRecipe();
        recipe.getEvals().remove(recipeEval);
        recipeRepository.save(recipe);
    }
}
