package com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NutrientInfoService {
    private final NutrientInfoRepository nutrientInfoRepository;
    public  void reset(){nutrientInfoRepository.deleteAll();}
    public NutrientInfo getNutrientInfo(Ingredient ingredient, Nutrient nutrient, double amount) {
        Optional<NutrientInfo> _info = nutrientInfoRepository.find(ingredient.getName(), nutrient.getName());
        if (_info.isPresent()) {
            NutrientInfo info = _info.get();
            if (info.getAmount() == amount)
                return info;
            else return null;
        } else
//            return nutrientInfoRepository.save(NutrientInfo.builder().ingredient(ingredient).nutrient(nutrient).amount(amount).build());
            return NutrientInfo.builder().ingredient(ingredient).nutrient(nutrient).amount(amount).build();
    }
    public NutrientInfo getModifiedNutrientInfo(Ingredient ingredient, Nutrient nutrient, double amount) {
        Optional<NutrientInfo> _info = nutrientInfoRepository.find(ingredient.getName(), nutrient.getName());
        if (_info.isPresent()) {
            NutrientInfo info = _info.get();
            if (info.getAmount() != amount){
                info.setAmount(amount);
//                nutrientInfoRepository.save(info);
            }
            return info;
        } else
//            return nutrientInfoRepository.save(NutrientInfo.builder().ingredient(ingredient).nutrient(nutrient).amount(amount).build());
            return NutrientInfo.builder().ingredient(ingredient).nutrient(nutrient).amount(amount).build();
    }


}
