package com.StarJ.food_recipe.Entities.Tags;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TagRepositoryCustomImpl implements  TagRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    QTag qTag = QTag.tag;
    @Override
    public Long getCount() {
        return jpaQueryFactory.select(qTag.count()).from(qTag).fetchOne();
    }
}
