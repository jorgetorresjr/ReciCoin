package org.ifpe.recicoin.controllers;

import jakarta.validation.Valid;
import org.ifpe.recicoin.entities.Company;
import org.ifpe.recicoin.entities.CompanyRegisterDTO;
import org.ifpe.recicoin.entities.LoginDTO;
import org.ifpe.recicoin.entities.LoginResponseDTO;
import org.ifpe.recicoin.repositories.CompanyRepository;
import org.ifpe.recicoin.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/company")
public class CompanyAuthController {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO login) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login.email(), login.password());
        var authentication = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Company) authentication.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CompanyRegisterDTO register) {
        if(companyRepository.findByEmail(register.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(register.password());

        var company = new Company();

        company.setLegalName(register.legalName());
        company.setEmail(register.email());
        company.setPassword(encryptedPassword);
        company.setPhone(register.phone());
        company.setAddress(register.address());
        company.setCity(register.city());
        company.setState(register.state());
        company.setZipcode(register.zipcode());
        company.setOpeningTime(register.openingTime());
        company.setClosingTime(register.closingTime());
        company.setRole(register.role());

        companyRepository.save(company);

        return ResponseEntity.ok().build();
    }

}
