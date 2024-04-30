package com.StarJ.food_recipe.Entities.PredictData;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PredictDatumController {
    private final PredictDatumService dataService;
    @PostMapping("/manager/training")
    public String training(){
        dataService.training();
        return "redirect:/manager";
    }
}
