package org.ifpe.recicoin.service;

import org.ifpe.recicoin.entities.DropoffAppointment;
import org.ifpe.recicoin.entities.User;
import org.ifpe.recicoin.repositories.DropoffAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DropoffAppointmentService {

    @Autowired
    private DropoffAppointmentRepository dropoffAppointmentRepository;

    public DropoffAppointment createDropoffAppointment(DropoffAppointment dropoffAppointment) {
        return dropoffAppointmentRepository.save(dropoffAppointment);
    }

    public DropoffAppointment updateDropoffAppointment(Long id, DropoffAppointment dropoffAppointment) {
        DropoffAppointment old = dropoffAppointmentRepository.findById(id).orElseThrow();

        old.setStatus(dropoffAppointment.getStatus());
        old.setScheduledDateTime(dropoffAppointment.getScheduledDateTime());

        return dropoffAppointmentRepository.save(old);
    }

    public void deleteDropoffAppointment(Long id) {
        dropoffAppointmentRepository.deleteById(id);
    }

    public DropoffAppointment getDropoffAppointment(Long id) {
        return dropoffAppointmentRepository.findById(id).orElseThrow();
    }

    public List<DropoffAppointment> findAll() {
        return dropoffAppointmentRepository.findAll();
    }

    public void validateDelivery(DropoffAppointment ap, User collector, double weight) {

        if (ap.isValidated())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entrega já confirmada");

        ap.setWeightKg(weight);
        ap.setValidated(true);
        ap.setValidatedBy(collector);
        ap.setValidatedAt(LocalDateTime.now());
    }


}
