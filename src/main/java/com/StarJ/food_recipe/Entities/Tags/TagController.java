package com.StarJ.food_recipe.Entities.Tags;

import com.StarJ.food_recipe.Entities.Categories.Category;
import com.StarJ.food_recipe.Entities.Categories.CategoryService;
import com.StarJ.food_recipe.Entities.Tags.Form.TagEditForm;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/manager")
public class TagController {
    private final TagService tagService;
    private final CategoryService categoryService;

    @ModelAttribute("categories")
    public List<Category> getCategories(){
        return categoryService.getCategories();
    }
    @GetMapping("/tag")
    public String tags(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("paging", tagService.getTags(page));
        return "managers/tags/tag";
    }

    @GetMapping("/tag/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, TagEditForm tagEditForm) {
        Tag tag = tagService.getTag(id);
        tagEditForm.setName(tag.getName());
        tagEditForm.setCategory(tag.getCategory().getName());
        model.addAttribute("destination", String.format("/manager/tag/%s", id));
        return "managers/tags/tag_post";
    }

    @PostMapping("/tag/{id}")
    public String edit(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id, @Valid TagEditForm tagEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", String.format("/manager/tag/%s", id));
            return "managers/tags/tag_post";
        }
        Tag tag = tagService.getTag(id);
        if (!tag.getName().equals(tagEditForm.getName()) && tagService.has(tagEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", String.format("/manager/tag/%s", id));
            return "managers/tags/tag_post";
        }

        tagService.modify(tag, principalDetail.getUser(), tagEditForm.getName(), tagEditForm.getCategory());
        return "redirect:/manager/tag";
    }

    @GetMapping("/tag/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Tag tag = tagService.getTag(id);
        tagService.delete(tag);
        return "redirect:/manager/tag";
    }

    @GetMapping("/tag/create")
    public String create(Model model, TagEditForm tagEditForm) {
        model.addAttribute("destination", "/manager/tag/create");
        return "managers/tags/tag_post";
    }

    @PostMapping("/tag/create")
    public String create(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @Valid TagEditForm tagEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", "/manager/tag/create");
            return "managers/tags/tag_post";
        }
        if (tagService.has(tagEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", "/manager/tag/create");
            return "managers/tags/tag_post";
        }
        tagService.create(principalDetail.getUser(), tagEditForm.getName(), tagEditForm.getCategory());
        return "redirect:/manager/tag";
    }
}
