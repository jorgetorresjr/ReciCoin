package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.Company;
import org.ifpe.recicoin.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public ResponseEntity<Company> getLoggedCompany() {
        Company company = (Company) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(company);
    }

    @PutMapping
    public ResponseEntity<Company> updateLoggedCompany(@RequestBody Company newCompany) {
        Company company = (Company) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        company.setLegalName(newCompany.getLegalName());
        company.setPhone(newCompany.getPhone());
        company.setEmail(newCompany.getEmail());
        company.setAddress(newCompany.getAddress());
        company.setCity(newCompany.getCity());
        company.setState(newCompany.getState());
        company.setZipcode(newCompany.getZipcode());
        company.setDescription(newCompany.getDescription());
        company.setOpeningTime(newCompany.getOpeningTime());
        company.setClosingTime(newCompany.getClosingTime());

        companyRepository.save(company);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Company> deleteLoggedCompany() {
        Company company = (Company) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        companyRepository.delete(company);

        return ResponseEntity.ok().build();
    }
}
