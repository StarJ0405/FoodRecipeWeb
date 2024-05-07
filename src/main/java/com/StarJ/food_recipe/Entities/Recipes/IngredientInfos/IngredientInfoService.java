package com.StarJ.food_recipe.Entities.Recipes.IngredientInfos;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IngredientInfoService {
    private final IngredientInfoRepository ingredientInfoRepository;
    public  void reset(){ingredientInfoRepository.deleteAll();}
    public IngredientInfo getIngredientInfo(Recipe recipe, Ingredient ingredient, double amount) {
        Optional<IngredientInfo> _info = ingredientInfoRepository.find(ingredient.getName(), recipe.getId());
        if (_info.isPresent()) {
            IngredientInfo info = _info.get();
            if (info.getAmount() == amount)
                return info;
            else return null;
        } else
            return ingredientInfoRepository.save(IngredientInfo.builder().ingredient(ingredient).recipe(recipe).amount(amount).build());
    }

    public IngredientInfo getModifiedIngredientInfo(Recipe recipe, Ingredient ingredient, double amount) {
        Optional<IngredientInfo> _info = ingredientInfoRepository.find(ingredient.getName(), recipe.getId());
        if (_info.isPresent()) {
            IngredientInfo info = _info.get();
            if (info.getAmount() != amount) {
                info.setAmount(amount);
                ingredientInfoRepository.save(info);
            }
            return info;
        } else
            return ingredientInfoRepository.save(IngredientInfo.builder().ingredient(ingredient).recipe(recipe).amount(amount).build());
    }
}
