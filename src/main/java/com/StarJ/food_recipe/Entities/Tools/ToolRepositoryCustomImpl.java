package com.StarJ.food_recipe.Entities.Tools;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ToolRepositoryCustomImpl implements  ToolRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    QTool qTool = QTool.tool;
    @Override
    public Long getCount() {
        return jpaQueryFactory.select(qTool.count()).from(qTool).fetchOne();
    }
}
