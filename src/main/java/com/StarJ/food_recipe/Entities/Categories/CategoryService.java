package com.StarJ.food_recipe.Entities.Categories;

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
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Page<Category> getCategories(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));
        return categoryRepository.findAll(pageable);
    }

    public Category getCategory(String name) {
        Optional<Category> _category = categoryRepository.findById(name);
        if (_category.isPresent())
            return _category.get();
        else
//            throw new DataNotFoundException("없는 데이터입니다.");
            return null;
    }

    public Category getCategory(Integer id) {
        Optional<Category> _category = categoryRepository.findById(id);
        if (_category.isPresent())
            return _category.get();
        else
            throw new DataNotFoundException("없는 데이터입니다.");
    }

    public void modify(Category category, SiteUser user, String name) {
        category.setModifier(user);
        category.setModifiedDate(LocalDateTime.now());
        category.setName(name);
        categoryRepository.save(category);
    }

    public boolean has(String name) {
        return categoryRepository.findById(name).isPresent();
    }

    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    public void create(SiteUser user, String name) {
        Optional<Category> _category = categoryRepository.findById(name);
        Category category = _category.orElseGet(() -> categoryRepository.save(Category.builder().author(user).build()));
    }
}
