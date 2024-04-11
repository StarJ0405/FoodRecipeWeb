package com.StarJ.food_recipe.Entities.Recipes.BodyImages;

import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BodyImageService {
    private final BodyImageRepository bodyImageRepository;

    public BodyImage getBodyImage(Recipe recipe, String body, String imgURL) {
        Optional<BodyImage> _bi = bodyImageRepository.find(recipe.getId(), body, imgURL);
        if (_bi.isPresent())
            return _bi.get();
        else
            return bodyImageRepository.save(BodyImage.builder().recipe(recipe).body(body).imgURL(imgURL).build());
    }
}
