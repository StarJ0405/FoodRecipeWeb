package com.StarJ.food_recipe.Entities.Tags;

import com.StarJ.food_recipe.Entities.Categories.Category;
import com.StarJ.food_recipe.Entities.Categories.CategoryService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;
    private final CategoryService categoryService;
    public Page<Tag> getTags(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));
        return tagRepository.findAll(pageable);
    }

    public Tag getTag(Integer id) {
        Optional<Tag> _tag = tagRepository.findById(id);
        if (_tag.isPresent())
            return _tag.get();
        else
            throw new DataNotFoundException("없는 데이터입니다.");
    }

    public void modify(Tag tag, SiteUser user, String name, String _category) {
        Category category = categoryService.getCategory(_category);
        tag.setModifier(user);
        tag.setModifiedDate(LocalDateTime.now());
        tag.setName(name);
        tag.setCategory(category);
        tagRepository.save(tag);
    }

    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }

    public void create(SiteUser user, String name, String _category) {
        Category category = categoryService.getCategory(_category);
        Tag tag = Tag.builder().author(user).name(name).category(category).build();
        tagRepository.save(tag);
    }
    public boolean has(String name) {
        return tagRepository.findById(name).isPresent();
    }
}
