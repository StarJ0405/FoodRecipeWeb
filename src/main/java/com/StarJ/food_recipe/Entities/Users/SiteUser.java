package com.StarJ.food_recipe.Entities.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @Column(length = 16)
    private String id;

    @Column(length = 16)
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
    @Builder

    public SiteUser(String id, String nickname, String password, String role, String email, LocalDateTime createDate, boolean locked, boolean emailVerified, String provider, String providerId) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.email = email;
        this.createDate = createDate;
        this.locked = locked;
        this.emailVerified = emailVerified;
        this.provider = provider;
        this.providerId = providerId;
    }
}
