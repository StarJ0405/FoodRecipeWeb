package com.StarJ.food_recipe.Entities.Recipes;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipe is a Querydsl query type for Recipe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipe extends EntityPathBase<Recipe> {

    private static final long serialVersionUID = 489039234L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipe recipe = new QRecipe("recipe");

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser author;

    public final StringPath baseImg = createString("baseImg");

    public final ListPath<com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImage, com.StarJ.food_recipe.Entities.Recipes.BodyImages.QBodyImage> bodyImages = this.<com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImage, com.StarJ.food_recipe.Entities.Recipes.BodyImages.QBodyImage>createList("bodyImages", com.StarJ.food_recipe.Entities.Recipes.BodyImages.BodyImage.class, com.StarJ.food_recipe.Entities.Recipes.BodyImages.QBodyImage.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final ListPath<com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEval, com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.QRecipeEval> evals = this.<com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEval, com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.QRecipeEval>createList("evals", com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEval.class, com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.QRecipeEval.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo, com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.QIngredientInfo> ingredientInfos = this.<com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo, com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.QIngredientInfo>createList("ingredientInfos", com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.IngredientInfo.class, com.StarJ.food_recipe.Entities.Recipes.IngredientInfos.QIngredientInfo.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public final com.StarJ.food_recipe.Entities.Users.QSiteUser modifier;

    public final StringPath subject = createString("subject");

    public final ListPath<com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTag, com.StarJ.food_recipe.Entities.Recipes.RecipeTags.QRecipeTag> tags = this.<com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTag, com.StarJ.food_recipe.Entities.Recipes.RecipeTags.QRecipeTag>createList("tags", com.StarJ.food_recipe.Entities.Recipes.RecipeTags.RecipeTag.class, com.StarJ.food_recipe.Entities.Recipes.RecipeTags.QRecipeTag.class, PathInits.DIRECT2);

    public final ListPath<com.StarJ.food_recipe.Entities.Recipes.RecipeTools.RecipeTool, com.StarJ.food_recipe.Entities.Recipes.RecipeTools.QRecipeTool> tools = this.<com.StarJ.food_recipe.Entities.Recipes.RecipeTools.RecipeTool, com.StarJ.food_recipe.Entities.Recipes.RecipeTools.QRecipeTool>createList("tools", com.StarJ.food_recipe.Entities.Recipes.RecipeTools.RecipeTool.class, com.StarJ.food_recipe.Entities.Recipes.RecipeTools.QRecipeTool.class, PathInits.DIRECT2);

    public final ComparablePath<java.util.UUID> UUID = createComparable("UUID", java.util.UUID.class);

    public QRecipe(String variable) {
        this(Recipe.class, forVariable(variable), INITS);
    }

    public QRecipe(Path<? extends Recipe> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipe(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipe(PathMetadata metadata, PathInits inits) {
        this(Recipe.class, metadata, inits);
    }

    public QRecipe(Class<? extends Recipe> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("author")) : null;
        this.modifier = inits.isInitialized("modifier") ? new com.StarJ.food_recipe.Entities.Users.QSiteUser(forProperty("modifier")) : null;
    }

}

