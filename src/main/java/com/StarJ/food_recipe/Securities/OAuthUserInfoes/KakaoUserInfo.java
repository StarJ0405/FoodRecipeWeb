package com.StarJ.food_recipe.Securities.OAuthUserInfoes;

import com.StarJ.food_recipe.Securities.OAuth2UserInfo;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private final String id;
    private final Map<String, Object> kakaoAccount;

    public KakaoUserInfo(Map<String, Object> attributes, String id ) {
        this.kakaoAccount = attributes;
        this.id = id;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.kakaoAccount;
    }

    @Override
    public String getProviderId() {
        return id;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return String.valueOf(kakaoAccount.get("email"));
    }

    @Override
    public String getName() {
        return null;
    }
}