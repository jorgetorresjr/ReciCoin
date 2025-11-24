package org.ifpe.recicoin.entities;

import java.time.LocalTime;

public record CompanyRegisterDTO(String legalName, String email, String phone, String address, String city, String state, String zipcode, String description, String password, LocalTime openingTime, LocalTime closingTime, UserRole role) {
}
