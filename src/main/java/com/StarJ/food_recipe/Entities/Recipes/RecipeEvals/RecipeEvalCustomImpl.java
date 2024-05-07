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
    QRecipeEval qRecipeEval = QRecipeEval.recipeEval;

    @Override
    public List<RecipeEval> findByRecipe(Recipe recipe) {
        return jpaQueryFactory.select(qRecipeEval).from(qRecipeEval).where(qRecipeEval.recipe.eq(recipe)).fetch();
    }

    @Override
    public List<RecipeEval> findByUser(SiteUser user) {
        return jpaQueryFactory.select(qRecipeEval).from(qRecipeEval).where(qRecipeEval.siteUser.eq(user)).fetch();
    }

    @Override
    public Optional<RecipeEval> findByUserRecipe(SiteUser user, Recipe recipe) {
        List<RecipeEval> list = jpaQueryFactory.select(qRecipeEval).from(qRecipeEval).where(qRecipeEval.siteUser.eq(user).and(qRecipeEval.recipe.eq(recipe))).fetch();
        if (!list.isEmpty())
            return Optional.ofNullable(list.getFirst());
        else
            return Optional.empty();
    }

    @Override
    public int getLastRecipeID() {
        return jpaQueryFactory.select(qRecipeEval.id.max()).from(qRecipeEval).fetchOne();
    }

    @Override
    public Long getCount() {
        return jpaQueryFactory.select(qRecipeEval.count()).from(qRecipeEval).fetchOne();
    }

    @Override
    public List<RecipeEval> findAfterIdByLimited(Integer id) {
        return jpaQueryFactory.select(qRecipeEval).from(qRecipeEval).where(qRecipeEval.id.gt(id)).limit(1000l).orderBy(qRecipeEval.id.asc()).fetch();
    }

    @Override
    public List<RecipeEval> findAllByLimited() {
        return jpaQueryFactory.select(qRecipeEval).from(qRecipeEval).limit(1000l).orderBy(qRecipeEval.id.asc()).fetch();
    }
}
