package com.StarJ.food_recipe.Entities.Units;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnitRepositoryCustomImpl implements UnitRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QUnit qUnit = QUnit.unit;
    @Override
    public Long getCount() {
        return jpaQueryFactory.select(qUnit.count()).from(qUnit).fetchOne();
    }
}
