package org.ifpe.recicoin.repositories;

import org.ifpe.recicoin.entities.CollectionPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionPointRepository extends JpaRepository<CollectionPoint, Long> {
    Optional<CollectionPoint> findByEmail(String email);
}