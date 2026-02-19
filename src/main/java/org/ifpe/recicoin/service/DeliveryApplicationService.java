package org.ifpe.recicoin.service;

import jakarta.transaction.Transactional;
import org.ifpe.recicoin.entities.DropoffAppointment;
import org.ifpe.recicoin.entities.User;
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

    @Autowired
    DropoffAppointmentRepository dropoffAppointmentRepository;

    @Transactional
    public void confirmDelivery(Long appointmentId, Long collectorId, double weight) {

        DropoffAppointment dropAppointment = dropoffAppointmentRepository.findById(appointmentId).orElseThrow();
        User collector = userRepository.getReferenceById(collectorId);
        DropoffAppointmentService dropoffService;

        dropoffAppointmentService.validateDelivery(dropAppointment, collector, weight);

        pointService.rewardDelivery(
                dropAppointment.getUser(),
                dropAppointment.getId(),
                dropAppointment.getMaterialType(),
                weight
        );
    }
}
