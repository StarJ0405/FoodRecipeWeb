package com.StarJ.food_recipe.Entities.Categories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QCategory qCategory = QCategory.category;
    @Override
    public Long getCount() {
        return jpaQueryFactory.select(qCategory.count()).from(qCategory).fetchOne();
    }
}
