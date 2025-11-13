package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.User;
import org.ifpe.recicoin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity getLoggedUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<?> updateLoggedUser(@RequestBody User newUser) {
        User oldUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        oldUser.setName(newUser.getName());
        oldUser.setCity(newUser.getCity());
        oldUser.setState(newUser.getState());
        oldUser.setPhone(newUser.getPhone());

        if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
            String encodedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
            oldUser.setPassword(encodedPassword);
        }

        userRepository.save(oldUser);

        return ResponseEntity.ok(oldUser);
    }

    @DeleteMapping
    public ResponseEntity deleteLoggedUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }
}
