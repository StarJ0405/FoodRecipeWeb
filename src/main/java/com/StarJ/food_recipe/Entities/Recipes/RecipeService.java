package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Ingredients.IngredientService;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImage;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImageService;
import com.StarJ.food_recipe.Entities.Recipes.BodyImages.Form.BodyImageForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.Form.IngredientInfoForm;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo;
import com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfoService;
import com.StarJ.food_recipe.Entities.Tags.Tag;
import com.StarJ.food_recipe.Entities.Tags.TagService;
import com.StarJ.food_recipe.Entities.Tools.Tool;
import com.StarJ.food_recipe.Entities.Tools.ToolService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Exceptions.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final TagService tagService;
    private final ToolService toolService;
    private final BodyImageService bodyImageService;
    private final IngredientService ingredientService;
    private final IngredientInfoService ingredientInfoService;
    @Autowired
    private ResourceLoader resourceLoader;

    public Page<Recipe> getRecipes(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));
        return recipeRepository.findAll(pageable);
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
        recipe.setTags(tagService.getTags(_tags));
        recipe.setTools(toolService.getTools(_tools));
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
        try {
            String path = resourceLoader.getResource("classpath:/static").getFile().getPath();
            File recipeFolder = new File(path + "/recipes/" + recipe.getUUID().toString());
            deleteFile(recipeFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    @Transactional
    public void create(SiteUser user, String subject, String baseImg, List<String> _tags, List<String> _tools, List<BodyImageForm> bodyImageForms, List<IngredientInfoForm> ingredientInfoForms) {
        List<Tag> tags = tagService.getTags(_tags);
        List<Tool> tools = toolService.getTools(_tools);
        Recipe recipe = Recipe.builder().author(user).subject(subject).tags(tags).uuid(UUID.randomUUID()).tools(tools).build();
        if (!baseImg.equals(""))
            try {
                String path = resourceLoader.getResource("classpath:/static").getFile().getPath();
                Path oldPath = Paths.get(path + baseImg);
                File recipeFolder = new File(path + "/recipes/" + recipe.getUUID().toString());
                if (!recipeFolder.exists())
                    recipeFolder.mkdirs();
                String newUrl = "/recipes/" + recipe.getUUID().toString() + "/baseImg" + baseImg.split("\\.")[1];
                Path newPath = Paths.get(path + newUrl);
                Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                recipe.setBaseImg(newUrl);
            } catch (IOException ex) {

            }
        List<BodyImage> bodyImages = recipe.getBodyImages();
        for (BodyImageForm form : bodyImageForms) {
            BodyImage bodyImage = bodyImageService.getBodyImage(recipe, form.getBody(), form.getImgURL());
            if (bodyImage != null)
                bodyImages.add(bodyImage);
        }
        List<IngredientInfo> ingredientInfos = recipe.getIngredientInfos();
        for (IngredientInfoForm form : ingredientInfoForms) {
            Ingredient ingredient = ingredientService.getIngredient(form.getIngredient());
            IngredientInfo ingredientInfo = ingredientInfoService.getIngredientInfo(recipe, ingredient, form.getAmount());
            if (ingredientInfo != null)
                ingredientInfos.add(ingredientInfo);
        }
        recipeRepository.save(recipe);
    }

    public String saveTempImage(MultipartFile file, SiteUser user) {
        if (!file.isEmpty())
            try {
                String path = resourceLoader.getResource("classpath:/static").getFile().getPath();
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
}
