package com.StarJ.food_recipe.Entities.PredictData;

import com.StarJ.food_recipe.Entities.Users.SiteUser;

import java.util.List;

public interface PredictDatumCustom {
    List<PredictDatum> getTop5(SiteUser user);
}
