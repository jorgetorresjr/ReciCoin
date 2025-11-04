package org.ifpe.recicoin.entities;

public enum UserRole {
    ADMIN("admin"),
    USER("user"),
    COMPANY("company"),
    COLLECTION_POINT("collection_point");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
