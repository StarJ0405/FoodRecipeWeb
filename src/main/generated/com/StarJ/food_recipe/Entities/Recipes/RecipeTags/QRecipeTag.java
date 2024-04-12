package com.StarJ.food_recipe.Entities.Recipes.RecipeTags;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipeTag is a Querydsl query type for RecipeTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipeTag extends EntityPathBase<RecipeTag> {

    private static final long serialVersionUID = 2140316057L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipeTag recipeTag = new QRecipeTag("recipeTag");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.StarJ.food_recipe.Entities.Recipes.QRecipe recipe;

    public final com.StarJ.food_recipe.Entities.Tags.QTag tag;

    public QRecipeTag(String variable) {
        this(RecipeTag.class, forVariable(variable), INITS);
    }

    public QRecipeTag(Path<? extends RecipeTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipeTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipeTag(PathMetadata metadata, PathInits inits) {
        this(RecipeTag.class, metadata, inits);
    }

    public QRecipeTag(Class<? extends RecipeTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recipe = inits.isInitialized("recipe") ? new com.StarJ.food_recipe.Entities.Recipes.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
        this.tag = inits.isInitialized("tag") ? new com.StarJ.food_recipe.Entities.Tags.QTag(forProperty("tag"), inits.get("tag")) : null;
    }

}

