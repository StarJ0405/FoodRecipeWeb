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
    QRecipe qRecipe = QRecipe.recipe;

    @Override
    public List<Recipe> search(SiteUser author) {
        return jpaQueryFactory.select(qRecipe).where(qRecipe.author.eq(author)).from(qRecipe).fetch();
    }

    @Override
    public Optional<Recipe> search(String subject, List<Ingredient> ingredients) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (Ingredient ingredient : ingredients)
            booleanBuilder.and(qRecipe.ingredientInfos.any().ingredient.eq(ingredient));
        List<Recipe> recipes = jpaQueryFactory.select(qRecipe).where(qRecipe.subject.eq(subject)).where(booleanBuilder).from(qRecipe).fetch();


        if (!recipes.isEmpty()) return Optional.ofNullable(recipes.getFirst());
        else return Optional.empty();
    }

    @Override
    public Optional<Recipe> search(String subject) {
        List<Recipe> recipes = jpaQueryFactory.select(qRecipe).where(qRecipe.subject.eq(subject)).from(qRecipe).fetch();

        Recipe recipe = null;
        if (!recipes.isEmpty()) recipe = recipes.getFirst();
        return Optional.ofNullable(recipe);
    }

    @Override
    public List<Integer> unseenSearch(String user) {
        QRecipeEval recipeEval = QRecipeEval.recipeEval;
        return jpaQueryFactory.select(qRecipe.id).from(qRecipe).where(qRecipe.notIn(jpaQueryFactory.select(recipeEval.recipe).from(recipeEval).where(recipeEval.siteUser.id.eq(user)))).fetch();
    }

    @Override
    public Page<Recipe> recipePage(Pageable pageable) {
        List<Recipe> content = jpaQueryFactory.select(qRecipe).from(qRecipe).offset(pageable.getOffset()).limit(pageable.getPageSize()).orderBy(qRecipe.createDate.desc()).fetch();
        JPAQuery<Long> count = jpaQueryFactory.select(qRecipe.count()).from(qRecipe);
//        return new PageImpl<>(content, pageable, count);
        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchOne());
    }

    @Override
    public Page<Recipe> recipePage(Pageable pageable, String kw) {
        List<Recipe> content = jpaQueryFactory.select(qRecipe).from(qRecipe).offset(pageable.getOffset()).where(qRecipe.subject.like("%" + kw + "%")).orderBy(qRecipe.createDate.desc()).limit(pageable.getPageSize()).fetch();
        JPAQuery<Long> count = jpaQueryFactory.select(qRecipe.count()).from(qRecipe);
        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchOne());
    }

    @Override
    public Page<Recipe> recipePage(Pageable pageable, String kw, List<String> tags) {
        List<Recipe> content = jpaQueryFactory.select(qRecipe).from(qRecipe).offset(pageable.getOffset()).where(qRecipe.subject.like("%" + kw + "%")).orderBy(qRecipe.createDate.desc()).where(qRecipe.tags.any().tag.name.in(tags)).limit(pageable.getPageSize()).fetch();
        JPAQuery<Long> count = jpaQueryFactory.select(qRecipe.count()).from(qRecipe);
        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchOne());
    }

    @Override
    public Long getCount() {
        return jpaQueryFactory.select(qRecipe.count()).from(qRecipe).fetchOne();
    }
}
