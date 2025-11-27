package org.ifpe.recicoin.service;

import org.ifpe.recicoin.entities.Company;
import org.ifpe.recicoin.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company newCompany) {
        Company oldCompany = companyRepository.findById(id).get();

        oldCompany.setLegalName(newCompany.getLegalName());
        oldCompany.setEmail(newCompany.getEmail());
        oldCompany.setPhone(newCompany.getPhone());
        oldCompany.setAddress(newCompany.getAddress());
        oldCompany.setCity(newCompany.getCity());
        oldCompany.setState(newCompany.getState());
        oldCompany.setZipcode(newCompany.getZipcode());
        oldCompany.setDescription(newCompany.getDescription());
        oldCompany.setPassword(newCompany.getPassword());
        oldCompany.setOpeningTime(newCompany.getOpeningTime());
        oldCompany.setClosingTime(newCompany.getClosingTime());

        return companyRepository.save(oldCompany);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public UserDetails findUserByEmail(String email) {
        return companyRepository.findByEmail(email);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }
}
