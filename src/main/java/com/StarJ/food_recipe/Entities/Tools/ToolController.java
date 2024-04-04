package com.StarJ.food_recipe.Entities.Tools;

import com.StarJ.food_recipe.Entities.Tools.Form.ToolEditForm;
import com.StarJ.food_recipe.Securities.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String post(Model model, @PathVariable("id") Integer name, ToolEditForm toolEditForm) {
        Tool tool = toolService.getTool(name);
        toolEditForm.setName(tool.getName());
        toolEditForm.setDescription(tool.getDescription());
//        model.addAttribute("tool", tool);
        return "managers/tools/tool_edit";
    }

    @PostMapping("/tool/{id}")
    public String post(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Integer id, @Param("name") String name, @Param("description") String description) {
        Tool tool = toolService.getTool(id);
        toolService.modify(tool, principalDetail.getUser(), name, description);
        return "redirect:/manager/tool";
    }

    @GetMapping("/tool/delete/{id}")
    public String delete(Model model, @PathVariable("id") Integer id) {
        Tool tool = toolService.getTool(id);
        toolService.delete(tool);
        return "redirect:/manager/tool";
    }

    @GetMapping("/tool/create")
    public String create(ToolEditForm toolEditForm) {
        return "managers/tools/tool_create";
    }

    @PostMapping("/tool/create")
    public String create(@AuthenticationPrincipal PrincipalDetail principalDetail, ToolEditForm toolEditForm) {
        toolService.create(principalDetail.getUser(), toolEditForm.getName(), toolEditForm.getDescription());
        return "redirect:/manager/tool";
    }
}
