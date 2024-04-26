package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Ingredients.Ingredient;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.QRecipeEval;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QRecipe recipe = QRecipe.recipe;

    @Override
    public List<Recipe> search(SiteUser author) {
        return jpaQueryFactory.select(recipe).where(recipe.author.eq(author)).from(recipe).fetch();
    }

    @Override
    public Optional<Recipe> search(String subject, List<Ingredient> ingredients) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (Ingredient ingredient : ingredients)
            booleanBuilder.and(recipe.ingredientInfos.any().ingredient.eq(ingredient));
        List<Recipe> recipes = jpaQueryFactory.select(recipe).where(recipe.subject.eq(subject)).where(booleanBuilder).from(recipe).fetch();


        if (!recipes.isEmpty()) return Optional.ofNullable(recipes.getFirst());
        else return Optional.empty();
    }

    @Override
    public Optional<Recipe> search(String subject) {
        List<Recipe> recipes = jpaQueryFactory.select(recipe).where(recipe.subject.eq(subject)).from(recipe).fetch();

        Recipe recipe = null;
        if (!recipes.isEmpty()) recipe = recipes.getFirst();
        return Optional.ofNullable(recipe);
    }

    @Override
    public List<Recipe> unseenSearch(SiteUser user) {
        QRecipeEval recipeEval = QRecipeEval.recipeEval;
        return jpaQueryFactory.select(recipe).from(recipe).where(recipe.notIn(jpaQueryFactory.select(recipeEval.recipe).from(recipeEval).where(recipeEval.siteUser.eq(user)))).fetch();
    }

    @Override
    public Page<Recipe> recipePage(Pageable pageable) {
        List<Recipe> content = jpaQueryFactory.select(recipe).from(recipe).offset(pageable.getOffset()).limit(pageable.getPageSize()).orderBy(recipe.createDate.desc()).fetch();
        JPAQuery<Long> count = jpaQueryFactory.select(recipe.count()).from(recipe);
//        return new PageImpl<>(content, pageable, count);
        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchOne());
    }

    @Override
    public Page<Recipe> recipePage(Pageable pageable, String kw) {
        List<Recipe> content = jpaQueryFactory.select(recipe).from(recipe).offset(pageable.getOffset()).where(recipe.subject.like("%" + kw + "%")).orderBy(recipe.createDate.desc()).limit(pageable.getPageSize()).fetch();
        JPAQuery<Long> count = jpaQueryFactory.select(recipe.count()).from(recipe);
        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchOne());
    }

    @Override
    public Page<Recipe> recipePage(Pageable pageable, String kw, List<String> tags) {
        List<Recipe> content = jpaQueryFactory.select(recipe).from(recipe).offset(pageable.getOffset()).where(recipe.subject.like("%" + kw + "%")).orderBy(recipe.createDate.desc()).where(recipe.tags.any().tag.name.in(tags)).limit(pageable.getPageSize()).fetch();
        JPAQuery<Long> count = jpaQueryFactory.select(recipe.count()).from(recipe);
        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchOne());
    }
}
