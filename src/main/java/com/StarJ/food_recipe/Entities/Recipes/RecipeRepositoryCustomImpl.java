package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QRecipe recipe = QRecipe.recipe;

    @Override
    public List<Recipe> search(SiteUser author) {
        return jpaQueryFactory.select(recipe)
                .where(recipe.author.eq(author)).from(recipe)
                .fetch();
    }

    @Override
    public Optional<Recipe> search(String subject, List<Ingredient> ingredients) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (Ingredient ingredient : ingredients)
            booleanBuilder.and(recipe.ingredientInfos.any().ingredient.eq(ingredient));
        List<Recipe> recipes = jpaQueryFactory.select(recipe).where(recipe.subject.eq(subject)).where(booleanBuilder).from(recipe).fetch();


        if (!recipes.isEmpty())
            return Optional.ofNullable(recipes.getFirst());
        else
            return Optional.empty();
    }

    @Override
    public Optional<Recipe> search(String subject) {
        List<Recipe> recipes = jpaQueryFactory.select(recipe).where(recipe.subject.eq(subject)).from(recipe).fetch();

        Recipe recipe = null;
        if (!recipes.isEmpty())
            recipe = recipes.getFirst();
        return Optional.ofNullable(recipe);
    }
}
