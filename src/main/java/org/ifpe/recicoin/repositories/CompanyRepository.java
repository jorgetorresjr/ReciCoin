package org.ifpe.recicoin.repositories;

import org.ifpe.recicoin.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email); // agora funciona com Optional<Company>
}