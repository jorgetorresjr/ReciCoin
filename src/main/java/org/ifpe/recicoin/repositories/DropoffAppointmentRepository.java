package org.ifpe.recicoin.repositories;

import org.ifpe.recicoin.entities.DropoffAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DropoffAppointmentRepository extends JpaRepository<DropoffAppointment, Long> {
}
