package org.ifpe.recicoin.repositories;

import org.ifpe.recicoin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    public UserDetails findByEmail(String email);
}
