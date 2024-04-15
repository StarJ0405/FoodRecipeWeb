package com.StarJ.food_recipe.Entities.Recipes.IngredientInfos;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIngredientInfo is a Querydsl query type for IngredientInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIngredientInfo extends EntityPathBase<IngredientInfo> {

    private static final long serialVersionUID = 1831901485L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIngredientInfo ingredientInfo = new QIngredientInfo("ingredientInfo");

    public final NumberPath<Double> amount = createNumber("amount", Double.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.StarJ.food_recipe.Entities.Ingredients.QIngredient ingredient;

    public final com.StarJ.food_recipe.Entities.Recipes.QRecipe recipe;

    public QIngredientInfo(String variable) {
        this(IngredientInfo.class, forVariable(variable), INITS);
    }

    public QIngredientInfo(Path<? extends IngredientInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIngredientInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIngredientInfo(PathMetadata metadata, PathInits inits) {
        this(IngredientInfo.class, metadata, inits);
    }

    public QIngredientInfo(Class<? extends IngredientInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ingredient = inits.isInitialized("ingredient") ? new com.StarJ.food_recipe.Entities.Ingredients.QIngredient(forProperty("ingredient"), inits.get("ingredient")) : null;
        this.recipe = inits.isInitialized("recipe") ? new com.StarJ.food_recipe.Entities.Recipes.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}

