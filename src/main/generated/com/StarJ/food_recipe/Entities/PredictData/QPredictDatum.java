package com.StarJ.food_recipe.Entities.PredictData;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPredictDatum is a Querydsl query type for PredictDatum
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPredictDatum extends EntityPathBase<PredictDatum> {

    private static final long serialVersionUID = -344886324L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPredictDatum predictDatum = new QPredictDatum("predictDatum");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Double> predict_val = createNumber("predict_val", Double.class);

    public final com.StarJ.food_recipe.Entities.Recipes.QRecipe recipe;

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser user;

    public QPredictDatum(String variable) {
        this(PredictDatum.class, forVariable(variable), INITS);
    }

    public QPredictDatum(Path<? extends PredictDatum> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPredictDatum(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPredictDatum(PathMetadata metadata, PathInits inits) {
        this(PredictDatum.class, metadata, inits);
    }

    public QPredictDatum(Class<? extends PredictDatum> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recipe = inits.isInitialized("recipe") ? new com.StarJ.food_recipe.Entities.Recipes.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
        this.user = inits.isInitialized("user") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("user")) : null;
    }

}

