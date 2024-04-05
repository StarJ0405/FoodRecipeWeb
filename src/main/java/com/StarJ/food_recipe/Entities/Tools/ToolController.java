package com.StarJ.food_recipe.Entities.Tools;

import com.StarJ.food_recipe.Entities.Categories.Category;
import com.StarJ.food_recipe.Entities.Categories.CategoryService;
import com.StarJ.food_recipe.Entities.Tools.Form.ToolEditForm;
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
public class ToolController {
    private final ToolService toolService;

    @GetMapping("/tool")
    public String tools(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("paging", toolService.getTools(page));
        return "managers/tools/tool";
    }

    @GetMapping("/tool/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, ToolEditForm toolEditForm) {
        Tool tool = toolService.getTool(id);
        toolEditForm.setName(tool.getName());
        toolEditForm.setDescription(tool.getDescription());
        model.addAttribute("destination", String.format("/manager/tool/%s", id));
        return "managers/tools/tool_post";
    }

    @PostMapping("/tool/{id}")
    public String edit(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id, @Valid ToolEditForm toolEditForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", String.format("/manager/tool/%s", id));
            return "managers/tools/tool_post";
        }
        Tool tool = toolService.getTool(id);
        if (!tool.getName().equals(toolEditForm.getName()) && toolService.has(toolEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", String.format("/manager/tool/%s", id));
            return "managers/tools/tool_post";
        }

        toolService.modify(tool, principalDetail.getUser(), toolEditForm.getName(), toolEditForm.getDescription());
        return "redirect:/manager/tool";
    }

    @GetMapping("/tool/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Tool tool = toolService.getTool(id);
        toolService.delete(tool);
        return "redirect:/manager/tool";
    }

    @GetMapping("/tool/create")
    public String create(Model model, ToolEditForm toolEditForm) {
        model.addAttribute("destination", "/manager/tool/create");
        return "managers/tools/tool_post";
    }

    @PostMapping("/tool/create")
    public String create(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @Valid  ToolEditForm toolEditForm,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("destination", "/manager/tool/create");
            return "managers/tools/tool_post";
        }
        if (toolService.has(toolEditForm.getName())) {
            bindingResult.rejectValue("name", "hasSameName", "중복 값입니다.");
            model.addAttribute("destination", "/manager/tool/create");
            return "managers/tools/tool_post";
        }
        toolService.create(principalDetail.getUser(), toolEditForm.getName(), toolEditForm.getDescription());
        return "redirect:/manager/tool";
    }
}
