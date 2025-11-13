package org.ifpe.recicoin.entities;

public record UserRegisterDTO(String name, String email, String password, String phone, String state, String city, Long points, UserRole role) {

}
