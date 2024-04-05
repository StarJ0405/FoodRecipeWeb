package com.StarJ.food_recipe.Entities.Categories;

import com.StarJ.food_recipe.Entities.Categories.Form.CategoryEditForm;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/manager")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public String categories(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("paging", categoryService.getCategories(page));
        return "managers/categories/category";
    }

    @GetMapping("/category/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, CategoryEditForm categoryEditForm) {
        Category category = categoryService.getCategory(id);
        categoryEditForm.setName(category.getName());
        model.addAttribute("destination", String.format("/manager/category/%s", id));
        return "managers/categories/category_post";
    }

    @PostMapping("/category/{id}")
    public String edit(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id, @Valid CategoryEditForm categoryEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", String.format("/manager/category/%s", id));
            return "managers/categories/category_post";
        }
        Category category = categoryService.getCategory(id);
        if (!category.getName().equals(categoryEditForm.getName()) && categoryService.has(categoryEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", String.format("/manager/category/%s", id));
            return "managers/categories/category_post";
        }
        categoryService.modify(category, principalDetail.getUser(), categoryEditForm.getName());
        return "redirect:/manager/category";
    }

    @GetMapping("/category/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Category category = categoryService.getCategory(id);
        categoryService.delete(category);
        return "redirect:/manager/category";
    }

    @GetMapping("/category/create")
    public String create(Model model, CategoryEditForm categoryEditForm) {
        model.addAttribute("destination", "/manager/category/create");
        return "managers/categories/category_post";
    }

    @PostMapping("/category/create")
    public String create(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @Valid CategoryEditForm categoryEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", "/manager/category/create");
            return "managers/categories/category_post";
        }
        if (categoryService.has(categoryEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", "/manager/category/create");
            return "managers/categories/category_post";
        }
        categoryService.create(principalDetail.getUser(), categoryEditForm.getName());
        return "redirect:/manager/category";
    }
}
