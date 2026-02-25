package org.ifpe.recicoin.service;

import jakarta.transaction.Transactional;
import org.ifpe.recicoin.entities.DropoffAppointment;
import org.ifpe.recicoin.entities.User;
import org.ifpe.recicoin.entities.enums.AppointmentStatus;
import org.ifpe.recicoin.repositories.DropoffAppointmentRepository;
import org.ifpe.recicoin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryApplicationService {

    @Autowired
    private DropoffAppointmentService dropoffAppointmentService;

    @Autowired
    private PointService pointService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void confirmDelivery(Long appointmentId, Long collectorId, double weight) {

        User collector = userRepository.getReferenceById(collectorId);

        DropoffAppointment ap = dropoffAppointmentService
                .completeDelivery(appointmentId, collector, weight);

        pointService.rewardDelivery(
                ap.getUser(),
                ap.getId(),
                ap.getMaterialType(),
                weight
        );
    }
}