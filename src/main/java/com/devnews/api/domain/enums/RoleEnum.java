package com.devnews.api.domain.enums;

public enum RoleEnum {
    ADMIN("Admin"),
    USER("User");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
