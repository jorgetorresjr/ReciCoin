package org.ifpe.recicoin.entities;

public record RegisterDTO (String email, String password, String phone, String state, String city, Long points, UserRole role) {

}
