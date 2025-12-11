package org.ifpe.recicoin.repositories;

import org.ifpe.recicoin.entities.CollectionPoint;
import org.ifpe.recicoin.entities.DropoffAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DropoffAppointmentRepository extends JpaRepository<DropoffAppointment, Long> {
    List<DropoffAppointment> findByCollectionPoint(CollectionPoint collectionPoint);
}