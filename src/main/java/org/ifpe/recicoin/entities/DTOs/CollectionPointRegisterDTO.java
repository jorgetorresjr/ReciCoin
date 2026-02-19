package org.ifpe.recicoin.entities.DTOs;

import org.ifpe.recicoin.entities.enums.UserRole;

import java.time.LocalTime;

public record CollectionPointRegisterDTO(String name, String email, String password, String phone, String address, String city, String state, String zipcode, String description, LocalTime openingTime, LocalTime closingTime, UserRole role) {
}
