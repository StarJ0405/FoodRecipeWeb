package com.StarJ.food_recipe.Entities.Tools;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTool is a Querydsl query type for Tool
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTool extends EntityPathBase<Tool> {

    private static final long serialVersionUID = -1256695338L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTool tool = new QTool("tool");

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser author;

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser modifier;

    public final StringPath name = createString("name");

    public QTool(String variable) {
        this(Tool.class, forVariable(variable), INITS);
    }

    public QTool(Path<? extends Tool> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTool(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTool(PathMetadata metadata, PathInits inits) {
        this(Tool.class, metadata, inits);
    }

    public QTool(Class<? extends Tool> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("author")) : null;
        this.modifier = inits.isInitialized("modifier") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("modifier")) : null;
    }

}

