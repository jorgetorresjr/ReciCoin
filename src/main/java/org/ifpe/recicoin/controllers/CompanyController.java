package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.Company;
import org.ifpe.recicoin.entities.DTOs.CompanyRegisterDTO;
import org.ifpe.recicoin.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CompanyRegisterDTO dto) {
        Company company = companyService.register(dto);
        return ResponseEntity.ok(company.getId());
    }
}