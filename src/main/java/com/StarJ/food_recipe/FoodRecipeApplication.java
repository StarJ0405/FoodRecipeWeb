package com.StarJ.food_recipe;

import com.StarJ.food_recipe.Global.Exceptions.DataNotFoundException;
import com.StarJ.food_recipe.Global.OSType;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodRecipeApplication {
    @Getter
    private static OSType OS_TYPE;

    public static void main(String[] args) {
        OS_TYPE = OSType.getInstance();
        if (!OS_TYPE.equals(OSType.ETC))
            SpringApplication.run(FoodRecipeApplication.class, args);
        else
            throw new DataNotFoundException("OS 정보를 불러오지 못했습니다.");
    }

}
