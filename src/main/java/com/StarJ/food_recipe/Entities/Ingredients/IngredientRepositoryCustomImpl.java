package com.StarJ.food_recipe.Entities.Ingredients;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IngredientRepositoryCustomImpl implements  IngredientRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    QIngredient qIngredient = QIngredient.ingredient;
    @Override
    public Long getCount() {
        return jpaQueryFactory.select(qIngredient.count()).from(qIngredient).fetchOne();
    }
}
