package org.ifpe.recicoin.repositories;

import org.ifpe.recicoin.entities.CollectionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface CollectionPointRepository extends JpaRepository<CollectionPoint, Long> {
    public UserDetails findByEmail(String email);
}
