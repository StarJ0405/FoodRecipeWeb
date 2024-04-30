package com.StarJ.food_recipe.Entities.Units;

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
public class UnitService {
    private final UnitRepository unitRepository;

    public List<Unit> getUnits() {
        return unitRepository.findAll();
    }

    public Page<Unit> getUnits(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));
        return unitRepository.findAll(pageable);
    }

    public Unit getUnit(Integer id) {
        Optional<Unit> _unit = unitRepository.findById(id);
        if (_unit.isPresent())
            return _unit.get();
        else
            throw new DataNotFoundException("없는 도구입니다.");
    }

    public Unit getUnit(String name) {
        Optional<Unit> _unit = unitRepository.findById(name);
        if (_unit.isPresent())
            return _unit.get();
        else
            return null;// throw new DataNotFoundException("없는 도구입니다.");
    }


    public void modify(Unit unit, SiteUser user, String name, String description) {
        unit.setModifier(user);
        unit.setModifiedDate(LocalDateTime.now());
        unit.setName(name);
        unit.setDescription(description);
        unitRepository.save(unit);
    }

    public void delete(Unit unit) {
        unitRepository.delete(unit);
    }

    public void create(SiteUser user, String name, String description) {
        Optional<Unit> _unit = unitRepository.findById(name);
        Unit unit = _unit.orElseGet(() -> Unit.builder().author(user).name(name).build());
        unit.setDescription(description);
        unitRepository.save(unit);
    }

    public boolean has(String name) {
        return unitRepository.findById(name).isPresent();
    }
}
