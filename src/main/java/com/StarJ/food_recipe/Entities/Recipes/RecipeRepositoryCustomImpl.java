package com.StarJ.food_recipe.Entities.Recipes;

import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@RequiredArgsConstructor
public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QRecipe recipe = QRecipe.recipe;

    @Override
    public List<Recipe> search(SiteUser author) {
        return jpaQueryFactory.select(recipe)
                .where(recipe.author.eq(author))
                .fetch();
    }
}
