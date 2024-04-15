package com.StarJ.food_recipe.Entities.Ingredients;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIngredient is a Querydsl query type for Ingredient
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIngredient extends EntityPathBase<Ingredient> {

    private static final long serialVersionUID = -178419192L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIngredient ingredient = new QIngredient("ingredient");

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser author;

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath info = createString("info");

    public final NumberPath<Double> kcal = createNumber("kcal", Double.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser modifier;

    public final StringPath name = createString("name");

    public final ListPath<com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo, com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.QNutrientInfo> nutrientInfos = this.<com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo, com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.QNutrientInfo>createList("nutrientInfos", com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.NutrientInfo.class, com.StarJ.food_recipe.Entities.Ingredients.NutrientInfos.QNutrientInfo.class, PathInits.DIRECT2);

    public final com.StarJ.food_recipe.Entities.Units.QUnit unit;

    public QIngredient(String variable) {
        this(Ingredient.class, forVariable(variable), INITS);
    }

    public QIngredient(Path<? extends Ingredient> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIngredient(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIngredient(PathMetadata metadata, PathInits inits) {
        this(Ingredient.class, metadata, inits);
    }

    public QIngredient(Class<? extends Ingredient> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("author")) : null;
        this.modifier = inits.isInitialized("modifier") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("modifier")) : null;
        this.unit = inits.isInitialized("unit") ? new com.StarJ.food_recipe.Entities.Units.QUnit(forProperty("unit"), inits.get("unit")) : null;
    }

}

