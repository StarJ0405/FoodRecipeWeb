package com.StarJ.food_recipe.Entities.Nutrients;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNutrient is a Querydsl query type for Nutrient
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNutrient extends EntityPathBase<Nutrient> {

    private static final long serialVersionUID = 1229075252L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNutrient nutrient = new QNutrient("nutrient");

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser author;

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser modifier;

    public final StringPath name = createString("name");

    public QNutrient(String variable) {
        this(Nutrient.class, forVariable(variable), INITS);
    }

    public QNutrient(Path<? extends Nutrient> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNutrient(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNutrient(PathMetadata metadata, PathInits inits) {
        this(Nutrient.class, metadata, inits);
    }

    public QNutrient(Class<? extends Nutrient> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("author")) : null;
        this.modifier = inits.isInitialized("modifier") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("modifier")) : null;
    }

}

