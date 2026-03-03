package org.ifpe.recicoin.repositories;

import org.ifpe.recicoin.entities.CollectionPoint;
import org.ifpe.recicoin.entities.DropoffAppointment;
import org.ifpe.recicoin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DropoffAppointmentRepository extends JpaRepository<DropoffAppointment, Long> {
    List<DropoffAppointment> findByCollectionPoint(CollectionPoint collectionPoint);
    List<DropoffAppointment> findByUser(User user);
    List<DropoffAppointment> findByUserId(Long userId);
    List<DropoffAppointment> findByCollectionPointId(Long pointId);
}