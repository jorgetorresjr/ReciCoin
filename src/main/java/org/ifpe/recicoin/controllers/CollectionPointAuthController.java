package org.ifpe.recicoin.controllers;

import jakarta.validation.Valid;
import org.ifpe.recicoin.entities.CollectionPoint;
import org.ifpe.recicoin.entities.CollectionPointRegisterDTO;
import org.ifpe.recicoin.entities.LoginDTO;
import org.ifpe.recicoin.entities.LoginResponseDTO;
import org.ifpe.recicoin.repositories.CollectionPointRepository;
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
@RequestMapping("auth/collection-point")
public class CollectionPointAuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CollectionPointRepository collectionPointRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO login) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login.email(), login.password());
        var authentication = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((CollectionPoint) authentication.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CollectionPointRegisterDTO register) {
        if (collectionPointRepository.findByEmail(register.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(register.password());

        var collectionPoint = new CollectionPoint();

        collectionPoint.setName(register.name());
        collectionPoint.setEmail(register.email());
        collectionPoint.setPassword(encryptedPassword);
        collectionPoint.setPhone(register.phone());
        collectionPoint.setAddress(register.address());
        collectionPoint.setCity(register.city());
        collectionPoint.setState(register.state());
        collectionPoint.setZipcode(register.zipcode());
        collectionPoint.setOpeningTime(register.openingTime());
        collectionPoint.setClosingTime(register.closingTime());
        collectionPoint.setRole(register.role());

        collectionPointRepository.save(collectionPoint);

        return ResponseEntity.ok().build();
    }

}
