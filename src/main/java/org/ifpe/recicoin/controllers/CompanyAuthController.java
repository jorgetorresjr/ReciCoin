package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.Company;
import org.ifpe.recicoin.entities.DTOs.LoginDTO;
import org.ifpe.recicoin.entities.DTOs.LoginResponseDTO;
import org.ifpe.recicoin.service.TokenService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/company")
public class CompanyAuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public CompanyAuthController(AuthenticationManager authenticationManager,
                                 TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO login) {
        var authToken = new UsernamePasswordAuthenticationToken(
                login.email(),
                login.password()
        );

        var authentication = authenticationManager.authenticate(authToken);

        Company company = (Company) authentication.getPrincipal();

        String token = tokenService.generateToken(company);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


}