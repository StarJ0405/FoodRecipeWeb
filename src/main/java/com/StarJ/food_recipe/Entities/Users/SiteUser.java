package com.StarJ.food_recipe.Entities.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SiteUser {
    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String nickname;

    @Column(columnDefinition = "TEXT")
    private String password;
    @Column(length = 20)
    private String role;

    @Column(columnDefinition = "TEXT")
    private String email;

    private LocalDateTime createDate;

    private boolean locked;
    private boolean emailVerified;

    private String provider;
    private String providerId;

    private String iconUrl;

    @Builder
    public SiteUser(String id, String nickname, String password, String role, String email, String provider, String providerId) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.email = email;
        this.createDate = LocalDateTime.now();
        this.locked = false;
        this.emailVerified = true;
        this.provider = provider;
        this.providerId = providerId;
        this.iconUrl = "/common/default.jpg";
    }
}
