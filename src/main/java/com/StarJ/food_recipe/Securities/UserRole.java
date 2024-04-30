package com.StarJ.food_recipe.Securities;

public enum UserRole {
    ADMIN,
    USER,
    MANAGER;


    private String value;

    public String getValue() {
        return "ROLE_" + this.name();
    }
    public static UserRole getUserRole(String role){
        for(UserRole userRole : values())
            if(role.toUpperCase().contains(userRole.name()))
                return userRole;
        return USER;
    }
}