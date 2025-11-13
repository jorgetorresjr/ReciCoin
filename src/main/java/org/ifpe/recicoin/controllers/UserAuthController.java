package org.ifpe.recicoin.controllers;

import jakarta.validation.Valid;
import org.ifpe.recicoin.entities.LoginDTO;
import org.ifpe.recicoin.entities.LoginResponseDTO;
import org.ifpe.recicoin.entities.UserRegisterDTO;
import org.ifpe.recicoin.entities.User;
import org.ifpe.recicoin.repositories.UserRepository;
import org.ifpe.recicoin.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class UserAuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO login) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login.email(), login.password());
        var authentication = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRegisterDTO register) {
        if(this.userRepository.findByEmail(register.email()) != null) {
            return ResponseEntity.badRequest().build();
        } else {
            String encryptedPassword = new BCryptPasswordEncoder().encode(register.password());

            var user = new User();
            user.setName(register.name());
            user.setEmail(register.email());
            user.setPassword(encryptedPassword);
            user.setPhone(register.phone());
            user.setState(register.state());
            user.setCity(register.city());
            user.setRole(register.role());

            userRepository.save(user);

            return ResponseEntity.ok().build();
        }
    }
}
