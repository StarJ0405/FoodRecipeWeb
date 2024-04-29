package com.StarJ.food_recipe.Global;

import lombok.Getter;

public enum OSType {
    Window("C:/web"),
    Linux(""),
    ETC("/");
    //

    @Getter
    private final String path;
    OSType(String path) {
        this.path=path;
    }
    public static OSType getInstance(){
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.toLowerCase().contains("win"))
            return Window;
        else {
            System.out.println(osName);
            return ETC;
        }
    }
}
