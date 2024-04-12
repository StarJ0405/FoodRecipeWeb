package com.StarJ.food_recipe.Entities.Recipes.BodyImages;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBodyImage is a Querydsl query type for BodyImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBodyImage extends EntityPathBase<BodyImage> {

    private static final long serialVersionUID = 1735934841L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBodyImage bodyImage = new QBodyImage("bodyImage");

    public final StringPath body = createString("body");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath imgURL = createString("imgURL");

    public final com.StarJ.food_recipe.Entities.Recipes.QRecipe recipe;

    public QBodyImage(String variable) {
        this(BodyImage.class, forVariable(variable), INITS);
    }

    public QBodyImage(Path<? extends BodyImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBodyImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBodyImage(PathMetadata metadata, PathInits inits) {
        this(BodyImage.class, metadata, inits);
    }

    public QBodyImage(Class<? extends BodyImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recipe = inits.isInitialized("recipe") ? new com.StarJ.food_recipe.Entities.Recipes.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}

