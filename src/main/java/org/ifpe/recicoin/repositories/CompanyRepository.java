package org.ifpe.recicoin.repositories;

import org.ifpe.recicoin.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    public UserDetails findByEmail(String email);
}
