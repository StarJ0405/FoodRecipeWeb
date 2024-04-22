package com.StarJ.food_recipe.Entities.PredictData;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictDatumRepository extends JpaRepository<PredictDatum, Integer>,PredictDatumCustom {
}
