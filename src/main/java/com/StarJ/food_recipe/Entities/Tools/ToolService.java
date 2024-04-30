package com.StarJ.food_recipe.Entities.Tools;

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
public class ToolService {
    private final ToolRepository toolRepository;

    public List<Tool> getTools() {
        return toolRepository.findAll();
    }

    public List<Tool> getTools(List<String> _tools) {
        return toolRepository.findByNames(_tools);
    }

    public Page<Tool> getTools(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));
        return toolRepository.findAll(pageable);
    }

    public Tool getTool(Integer id) {
        Optional<Tool> _tool = toolRepository.findById(id);
        if (_tool.isPresent())
            return _tool.get();
        else
            throw new DataNotFoundException("없는 도구입니다.");
    }

    public void modify(Tool tool, SiteUser user, String name, String description) {
        tool.setModifier(user);
        tool.setModifiedDate(LocalDateTime.now());
        tool.setName(name);
        tool.setDescription(description);
        toolRepository.save(tool);
    }

    public void delete(Tool tool) {
        toolRepository.delete(tool);
    }

    public void create(SiteUser user, String name, String description) {
        Optional<Tool> _tool = toolRepository.findById(name);
        Tool tool = _tool.orElseGet(() -> Tool.builder().author(user).name(name).build());
        tool.setDescription(description);
        toolRepository.save(tool);
    }

    public boolean has(String name) {
        return toolRepository.findById(name).isPresent();
    }
}
