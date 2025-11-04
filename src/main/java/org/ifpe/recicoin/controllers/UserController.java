package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.User;
import org.ifpe.recicoin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity getUser(Long idUser) {
        User user = userRepository.findById(idUser).get();

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) {
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
