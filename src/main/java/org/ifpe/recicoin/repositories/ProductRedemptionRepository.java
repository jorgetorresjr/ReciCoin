package org.ifpe.recicoin.repositories;

import org.ifpe.recicoin.entities.ProductRedemption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRedemptionRepository extends JpaRepository<ProductRedemption, Long> {
    List<ProductRedemption> findByUserIdOrderByCreatedAtDesc(Long userId);
}