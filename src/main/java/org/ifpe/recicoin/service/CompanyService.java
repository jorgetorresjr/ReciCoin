package org.ifpe.recicoin.service;

import org.ifpe.recicoin.entities.Company;
import org.ifpe.recicoin.entities.DTOs.CompanyRegisterDTO;
import org.ifpe.recicoin.entities.enums.UserRole;
import org.ifpe.recicoin.repositories.CompanyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyService(CompanyRepository companyRepository,
                          PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Company register(CompanyRegisterDTO dto) {

        if (companyRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        Company company = new Company();
        company.setLegalName(dto.legalName());
        company.setEmail(dto.email());
        company.setPhone(dto.phone());
        company.setAddress(dto.address());
        company.setCity(dto.city());
        company.setState(dto.state());
        company.setZipcode(dto.zipcode());
        company.setDescription(dto.description());
        company.setPassword(passwordEncoder.encode(dto.password()));
        company.setRole(UserRole.COMPANY);

        return companyRepository.save(company);
    }
}