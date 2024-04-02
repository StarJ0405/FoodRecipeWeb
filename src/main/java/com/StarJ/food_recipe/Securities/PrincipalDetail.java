package com.StarJ.food_recipe.Securities;

import com.StarJ.food_recipe.Entities.Users.SiteUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetail implements UserDetails, OAuth2User {

    private SiteUser user;
    private Map<String, Object> attributes;


    //생성자
    //일반 로그인
    public PrincipalDetail(SiteUser user) {
        this.user = user;
    }

    //OAuth 로그인
    public PrincipalDetail(SiteUser user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }


    //OAuth2User의 메서드
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override // 닉네임
    public String getName() {
        return user.getNickname();
    }


    //UserDetails의 메서드
    @Override // 해당 유저의 권한 목록
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override // 비밀번호
    public String getPassword() {
        return user.getPassword();
    }

    @Override // PK 값
    public String getUsername() {
        return user.getId();
    }


    @Override  // 계정 만료 여부
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 계정 잠김 여부
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    @Override // 비밀번호 만료 여부
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override // 사용자 활성화 여부
    public boolean isEnabled() {
        return user.isEmailVerified() && !user.isLocked();
    }
}