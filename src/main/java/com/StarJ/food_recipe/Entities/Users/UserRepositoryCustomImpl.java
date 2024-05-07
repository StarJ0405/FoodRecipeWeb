package com.StarJ.food_recipe.Entities.Users;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QSiteUser qSiteUser = QSiteUser.siteUser;
    @Override
    public Long getCount() {
        return jpaQueryFactory.select(qSiteUser.count()).from(qSiteUser).fetchOne();
    }

    @Override
    public List<String> getUsersId() {
        return jpaQueryFactory.select(qSiteUser.id).from(qSiteUser).fetch();
    }
}
