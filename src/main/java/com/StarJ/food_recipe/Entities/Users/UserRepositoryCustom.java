package com.StarJ.food_recipe.Entities.Users;

import java.util.List;

public interface UserRepositoryCustom {
    Long getCount();
    List<String> getUsersId();
}
