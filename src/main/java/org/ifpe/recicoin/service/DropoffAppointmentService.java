package org.ifpe.recicoin.service;

import org.ifpe.recicoin.entities.DropoffAppointment;
import org.ifpe.recicoin.repositories.DropoffAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropoffAppointmentService {
    @Autowired
    private DropoffAppointmentRepository dropoffAppointmentRepository;

    public DropoffAppointment createDropoffAppointment(DropoffAppointment dropoffAppointment) {
        return dropoffAppointmentRepository.save(dropoffAppointment);
    }

    public DropoffAppointment updateDropoffAppointment(Long id, DropoffAppointment dropoffAppointment) {
        DropoffAppointment oldDropoffAppointment = dropoffAppointmentRepository.findById(id).get();

        oldDropoffAppointment.setStatus(dropoffAppointment.getStatus());
        oldDropoffAppointment.setScheduledDateTime(dropoffAppointment.getScheduledDateTime());
        oldDropoffAppointment.setPointsAwarded(dropoffAppointment.getPointsAwarded());

        return dropoffAppointmentRepository.save(oldDropoffAppointment);
    }

    public void deleteDropoffAppointment(Long id) {
        dropoffAppointmentRepository.deleteById(id);
    }

    public DropoffAppointment getDropoffAppointment(Long id) {
        return dropoffAppointmentRepository.findById(id).get();
    }

    public List<DropoffAppointment> findAll() {
        return dropoffAppointmentRepository.findAll();
    }
}
