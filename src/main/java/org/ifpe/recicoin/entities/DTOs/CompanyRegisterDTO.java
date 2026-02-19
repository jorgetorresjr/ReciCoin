package org.ifpe.recicoin.entities.DTOs;

import org.ifpe.recicoin.entities.enums.UserRole;

import java.time.LocalTime;

public record CompanyRegisterDTO(String legalName, String email, String phone, String address, String city, String state, String zipcode, String description, String password, LocalTime openingTime, LocalTime closingTime, UserRole role) {
}
