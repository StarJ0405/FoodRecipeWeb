package com.StarJ.food_recipe.Entities.Ingredients;

import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.Form.NutrientInfoForm;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo;
import com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfoService;
import com.StarJ.food_recipe.Entities.Nutrients.Nutrient;
import com.StarJ.food_recipe.Entities.Nutrients.NutrientService;
import com.StarJ.food_recipe.Entities.Units.Unit;
import com.StarJ.food_recipe.Entities.Units.UnitService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Global.Exceptions.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final UnitService unitService;
    private final NutrientService nutrientService;
    private final NutrientInfoService nutrientInfoService;
    public  void reset(){ingredientRepository.deleteAll();}
    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }
    public Long getCount(){return  ingredientRepository.getCount();}
    public Page<Ingredient> getIngredients(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));
        return ingredientRepository.findAll(pageable);
    }

    public Ingredient getIngredient(String name) {
        Optional<Ingredient> _ingredient = ingredientRepository.findById(name);
        if (_ingredient.isPresent())
            return _ingredient.get();
        else
            throw new DataNotFoundException("없는 데이터입니다.");
    }

    public Ingredient getIngredient(Integer id) {
        Optional<Ingredient> _ingredient = ingredientRepository.findById(id);
        if (_ingredient.isPresent())
            return _ingredient.get();
        else
            throw new DataNotFoundException("없는 데이터입니다.");
    }

    public void modify(Ingredient ingredient, SiteUser user, String name, String info, double kcal, String _unit, List<NutrientInfoForm> nutrients) {
        Unit unit = unitService.getUnit(_unit);
        ingredient.setModifier(user);
        ingredient.setModifiedDate(LocalDateTime.now());
        ingredient.setName(name);
        ingredient.setInfo(info);
        ingredient.setKcal(kcal);
        ingredient.setUnit(unit);
        List<NutrientInfo> nutrientInfos = ingredient.getNutrientInfos();
        nutrientInfos.clear();
        for (NutrientInfoForm form : nutrients) {
            Nutrient nutrient = nutrientService.getNutrient(form.getNutrient());
            NutrientInfo nutrientInfo = nutrientInfoService.getModifiedNutrientInfo(ingredient, nutrient, form.getAmount());
            if (nutrientInfo != null)
                nutrientInfos.add(nutrientInfo);
        }
        ingredientRepository.save(ingredient);
    }

    public boolean has(String name) {
        return ingredientRepository.findById(name).isPresent();
    }

    public void delete(Ingredient ingredient) {
        ingredientRepository.delete(ingredient);
    }

    @Transactional
    public void create(SiteUser user, String name, String info, double kcal, String _unit, List<NutrientInfoForm> nutrients) {
        Optional<Ingredient> _ingredient = ingredientRepository.findById(name);
        Unit unit = unitService.getUnit(_unit);
        Ingredient ingredient = _ingredient.orElseGet(() -> Ingredient.builder().author(user).name(name).build());
        ingredient.setInfo(info);
        ingredient.setKcal(kcal);
        ingredient.setUnit(unit);
        List<NutrientInfo> nutrientInfos = ingredient.getNutrientInfos();
        for (NutrientInfoForm form : nutrients) {
            Nutrient nutrient = nutrientService.getNutrient(form.getNutrient());
            NutrientInfo nutrientInfo = nutrientInfoService.getNutrientInfo(ingredient, nutrient, form.getAmount());
            if (nutrientInfo != null)
                nutrientInfos.add(nutrientInfo);
        }
        ingredientRepository.save(ingredient);
    }
}
