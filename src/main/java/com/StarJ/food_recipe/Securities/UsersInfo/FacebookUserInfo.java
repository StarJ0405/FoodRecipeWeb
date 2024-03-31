package com.StarJ.food_recipe.Securities.UsersInfo;

import com.StarJ.food_recipe.Securities.OAuth2UserInfo;

import java.util.Map;

public class FacebookUserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    public FacebookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }
}
