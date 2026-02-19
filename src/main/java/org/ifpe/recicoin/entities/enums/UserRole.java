package org.ifpe.recicoin.entities.enums;

public enum UserRole {
    ADMIN("admin"),
    USER("user"),
    COMPANY("company"),
    COLLECTION_POINT("collection_point");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
