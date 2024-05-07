package com.StarJ.food_recipe.Entities.Nutrients;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NutrientRepositoryCustomImpl implements NutrientRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    QNutrient qNutrient = QNutrient.nutrient;

    @Override
    public Long getCount() {
        return jpaQueryFactory.select(qNutrient.count()).from(qNutrient).fetchOne();
    }
}
