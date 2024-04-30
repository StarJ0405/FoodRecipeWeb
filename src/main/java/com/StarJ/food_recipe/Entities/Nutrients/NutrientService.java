package com.StarJ.food_recipe.Entities.Nutrients;

import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Global.Exceptions.DataNotFoundException;
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
public class NutrientService {
    private final NutrientRepository nutrientRepository;

    public List<Nutrient> getNutrients() {
        return nutrientRepository.findAll();
    }

    public Page<Nutrient> getNutrients(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));
        return nutrientRepository.findAll(pageable);
    }
    public Nutrient getNutrient(String name) {
        Optional<Nutrient> _nutrient = nutrientRepository.findById(name);
        if (_nutrient.isPresent())
            return _nutrient.get();
        else
            throw new DataNotFoundException("없는 데이터입니다.");
    }
    public Nutrient getNutrient(Integer id) {
        Optional<Nutrient> _nutrient = nutrientRepository.findById(id);
        if (_nutrient.isPresent())
            return _nutrient.get();
        else
            throw new DataNotFoundException("없는 데이터입니다.");
    }

    public void modify(Nutrient nutrient, SiteUser user, String name, String description) {
        nutrient.setModifier(user);
        nutrient.setModifiedDate(LocalDateTime.now());
        nutrient.setName(name);
        nutrient.setDescription(description);
        nutrientRepository.save(nutrient);
    }

    public void delete(Nutrient nutrient) {
        nutrientRepository.delete(nutrient);
    }

    public void create(SiteUser user, String name, String description) {
        Optional<Nutrient> _nutrient = nutrientRepository.findById(name);
        Nutrient nutrient = _nutrient.orElseGet(()-> Nutrient.builder().author(user).name(name).build());
        nutrient.setDescription(description);
        nutrientRepository.save(nutrient);
    }

    public boolean has(String name) {
        return nutrientRepository.findById(name).isPresent();
    }
}
