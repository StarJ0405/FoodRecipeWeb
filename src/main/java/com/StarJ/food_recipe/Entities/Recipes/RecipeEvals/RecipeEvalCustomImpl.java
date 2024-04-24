package com.StarJ.food_recipe.Entities.Recipes.RecipeEvals;

import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RecipeEvalCustomImpl implements RecipeEvalCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QRecipeEval recipeEval = QRecipeEval.recipeEval;

    @Override
    public List<RecipeEval> findByRecipe(Recipe recipe) {
        return jpaQueryFactory.select(recipeEval).from(recipeEval).where(recipeEval.recipe.eq(recipe)).fetch();
    }

    @Override
    public List<RecipeEval> findByUser(SiteUser user) {
        return jpaQueryFactory.select(recipeEval).from(recipeEval).where(recipeEval.siteUser.eq(user)).fetch();
    }

    @Override
    public Optional<RecipeEval> findByUserRecipe(SiteUser user, Recipe recipe) {
        List<RecipeEval> list = jpaQueryFactory.select(recipeEval).from(recipeEval).where(recipeEval.siteUser.eq(user).and(recipeEval.recipe.eq(recipe))).fetch();
        if (!list.isEmpty())
            return Optional.ofNullable(list.getFirst());
        else
            return Optional.empty();
    }

    @Override
    public List<RecipeEval> findAfterId(Integer id) {
        return jpaQueryFactory.select(recipeEval).from(recipeEval).where(recipeEval.id.gt(id)).fetch();
    }

    @Override
    public int getLastRecipeID() {
        return jpaQueryFactory.select(recipeEval.id.max()).from(recipeEval).fetchOne();
    }
}
