package com.StarJ.food_recipe.Securities;

public enum UserRole {
    ADMIN,
    USER,
    MANAGER;


    private String value;

    public String getValue() {
        return "ROLE_" + this.name();
    }
}