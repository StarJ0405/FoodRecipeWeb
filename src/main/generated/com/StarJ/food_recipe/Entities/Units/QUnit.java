package com.StarJ.food_recipe.Entities.Units;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUnit is a Querydsl query type for Unit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUnit extends EntityPathBase<Unit> {

    private static final long serialVersionUID = 1292409006L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUnit unit = new QUnit("unit");

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser author;

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser modifier;

    public final StringPath name = createString("name");

    public QUnit(String variable) {
        this(Unit.class, forVariable(variable), INITS);
    }

    public QUnit(Path<? extends Unit> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUnit(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUnit(PathMetadata metadata, PathInits inits) {
        this(Unit.class, metadata, inits);
    }

    public QUnit(Class<? extends Unit> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("author")) : null;
        this.modifier = inits.isInitialized("modifier") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("modifier")) : null;
    }

}

