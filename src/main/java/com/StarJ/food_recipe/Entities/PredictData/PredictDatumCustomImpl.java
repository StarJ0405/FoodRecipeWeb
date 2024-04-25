package com.StarJ.food_recipe.Entities.PredictData;

import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PredictDatumCustomImpl implements PredictDatumCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QPredictDatum qPredictDatum = QPredictDatum.predictDatum;

    @Override
    public List<PredictDatum> getTop5(SiteUser user) {
        List<PredictDatum> predictData = jpaQueryFactory.select(qPredictDatum).from(qPredictDatum).where(qPredictDatum.user.eq(user)).orderBy(new OrderSpecifier<>(Order.DESC, qPredictDatum.predict_val)).fetch();
        return predictData.size() > 5 ? predictData.subList(0, 5) : predictData;
    }

    @Override
    public List<PredictDatum> getTop10() {
        List<PredictDatum> predictData = jpaQueryFactory.select(qPredictDatum).from(qPredictDatum).orderBy(new OrderSpecifier<>(Order.DESC, qPredictDatum.predict_val)).fetch();
        return predictData.size() > 10 ? predictData.subList(0, 10) : predictData;
    }
}
