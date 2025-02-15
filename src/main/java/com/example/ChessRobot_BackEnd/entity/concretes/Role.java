package com.example.ChessRobot_BackEnd.entity.concretes;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER"),
    ROLE_MODERATOR("MODERATOR");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
