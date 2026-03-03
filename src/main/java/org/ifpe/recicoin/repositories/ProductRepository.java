package org.ifpe.recicoin.repositories;

import java.util.List;
import org.ifpe.recicoin.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCompanyId(Long companyId);
}