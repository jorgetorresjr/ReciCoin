package org.ifpe.recicoin.entities.DTOs;

import org.ifpe.recicoin.entities.enums.UserRole;

public record UserRegisterDTO(String name, String email, String password, String phone, String state, String city, Long points, UserRole role) {

}
