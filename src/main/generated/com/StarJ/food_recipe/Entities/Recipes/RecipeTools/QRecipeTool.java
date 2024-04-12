package com.StarJ.food_recipe.Entities.Recipes.RecipeTools;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipeTool is a Querydsl query type for RecipeTool
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipeTool extends EntityPathBase<RecipeTool> {

    private static final long serialVersionUID = -1263277061L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipeTool recipeTool = new QRecipeTool("recipeTool");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.StarJ.food_recipe.Entities.Recipes.QRecipe recipe;

    public final com.StarJ.food_recipe.Entities.Tools.QTool tool;

    public QRecipeTool(String variable) {
        this(RecipeTool.class, forVariable(variable), INITS);
    }

    public QRecipeTool(Path<? extends RecipeTool> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipeTool(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipeTool(PathMetadata metadata, PathInits inits) {
        this(RecipeTool.class, metadata, inits);
    }

    public QRecipeTool(Class<? extends RecipeTool> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recipe = inits.isInitialized("recipe") ? new com.StarJ.food_recipe.Entities.Recipes.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
        this.tool = inits.isInitialized("tool") ? new com.StarJ.food_recipe.Entities.Tools.QTool(forProperty("tool"), inits.get("tool")) : null;
    }

}

