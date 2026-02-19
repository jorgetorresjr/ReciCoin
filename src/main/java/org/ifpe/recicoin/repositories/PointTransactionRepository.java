package org.ifpe.recicoin.repositories;


import org.ifpe.recicoin.entities.PointTransaction;
import org.ifpe.recicoin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM PointTransaction t
        WHERE t.user.id = :userId
""")
    Integer getUserAmount(Long userId);

    List<PointTransaction> findByUserIdOrderByCreatedAtDesc(Long userId);
}
