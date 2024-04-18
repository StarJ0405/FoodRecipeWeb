package com.StarJ.food_recipe.Entities.Recipes.RecipeEvals;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipeEval is a Querydsl query type for RecipeEval
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipeEval extends EntityPathBase<RecipeEval> {

    private static final long serialVersionUID = -1744825341L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipeEval recipeEval = new QRecipeEval("recipeEval");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.StarJ.food_recipe.Entities.Recipes.QRecipe recipe;

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser siteUser;

    public final NumberPath<Double> val = createNumber("val", Double.class);

    public QRecipeEval(String variable) {
        this(RecipeEval.class, forVariable(variable), INITS);
    }

    public QRecipeEval(Path<? extends RecipeEval> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipeEval(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipeEval(PathMetadata metadata, PathInits inits) {
        this(RecipeEval.class, metadata, inits);
    }

    public QRecipeEval(Class<? extends RecipeEval> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recipe = inits.isInitialized("recipe") ? new com.StarJ.food_recipe.Entities.Recipes.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
        this.siteUser = inits.isInitialized("siteUser") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("siteUser")) : null;
    }

}

