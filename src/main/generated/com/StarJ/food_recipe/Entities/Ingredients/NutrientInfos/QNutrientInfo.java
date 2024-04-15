package com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNutrientInfo is a Querydsl query type for NutrientInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNutrientInfo extends EntityPathBase<NutrientInfo> {

    private static final long serialVersionUID = 318879132L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNutrientInfo nutrientInfo = new QNutrientInfo("nutrientInfo");

    public final NumberPath<Double> amount = createNumber("amount", Double.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.StarJ.food_recipe.Entities.Ingredients.QIngredient ingredient;

    public final com.StarJ.food_recipe.Entities.Nutrients.QNutrient nutrient;

    public QNutrientInfo(String variable) {
        this(NutrientInfo.class, forVariable(variable), INITS);
    }

    public QNutrientInfo(Path<? extends NutrientInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNutrientInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNutrientInfo(PathMetadata metadata, PathInits inits) {
        this(NutrientInfo.class, metadata, inits);
    }

    public QNutrientInfo(Class<? extends NutrientInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ingredient = inits.isInitialized("ingredient") ? new com.StarJ.food_recipe.Entities.Ingredients.QIngredient(forProperty("ingredient"), inits.get("ingredient")) : null;
        this.nutrient = inits.isInitialized("nutrient") ? new com.StarJ.food_recipe.Entities.Nutrients.QNutrient(forProperty("nutrient"), inits.get("nutrient")) : null;
    }

}

