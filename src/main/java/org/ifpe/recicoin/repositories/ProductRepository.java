package org.ifpe.recicoin.repositories;

import org.ifpe.recicoin.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}