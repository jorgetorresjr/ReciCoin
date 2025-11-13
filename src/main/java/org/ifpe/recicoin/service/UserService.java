package org.ifpe.recicoin.service;

import org.ifpe.recicoin.entities.User;
import org.ifpe.recicoin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public User updateUser(Long id, User newUser) {
        User oldUser = userRepository.findById(id).get();

        oldUser.setName(newUser.getName());
        oldUser.setPhone(newUser.getPhone());
        oldUser.setState(newUser.getState());
        oldUser.setCity(newUser.getCity());

        return userRepository.save(oldUser);
    }

    public void changePassword(Long id, String oldPassword, String newPassword) {
        User oldUser = userRepository.findById(id).get();

        if (!passwordEncoder.matches(oldPassword, oldUser.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        oldUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(oldUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserDetails findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
