package org.ifpe.recicoin.service;

import org.ifpe.recicoin.entities.CollectionPoint;
import org.ifpe.recicoin.entities.DropoffAppointment;
import org.ifpe.recicoin.entities.User;
import org.ifpe.recicoin.entities.enums.AppointmentStatus;
import org.ifpe.recicoin.entities.enums.MaterialType;
import org.ifpe.recicoin.repositories.CollectionPointRepository;
import org.ifpe.recicoin.repositories.DropoffAppointmentRepository;
import org.ifpe.recicoin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DropoffAppointmentService {

    @Autowired
    private DropoffAppointmentRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionPointRepository pointRepository;

    @Transactional
    public DropoffAppointment schedule(Long userId,
                                       Long pointId,
                                       LocalDateTime dateTime,
                                       String description,
                                       MaterialType materialType) {

        User user = userRepository.getReferenceById(userId);
        CollectionPoint point = pointRepository.getReferenceById(pointId);

        DropoffAppointment ap = new DropoffAppointment();
        ap.setUser(user);
        ap.setCollectionPoint(point);
        ap.setScheduledDateTime(dateTime);
        ap.setDescription(description);
        ap.setMaterialType(materialType);
        ap.setStatus(AppointmentStatus.SCHEDULED);

        return repository.save(ap);
    }

    @Transactional
    public void accept(Long appointmentId, Long pointId) {

        DropoffAppointment ap = repository.findById(appointmentId).orElseThrow();

        if (!ap.getCollectionPoint().getId().equals(pointId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este ponto não pode aceitar este agendamento");

        if (ap.getStatus() != AppointmentStatus.SCHEDULED)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamento não está aguardando confirmação");

        ap.setStatus(AppointmentStatus.CONFIRMED);
        repository.save(ap);
    }

    @Transactional
    public void cancel(Long appointmentId, Long userId) {

        DropoffAppointment ap = repository.findById(appointmentId).orElseThrow();

        if (!ap.getUser().getId().equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode cancelar este agendamento");

        if (ap.getStatus() == AppointmentStatus.COMPLETED)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entrega já concluída");

        if (ap.getStatus() == AppointmentStatus.CANCELLED)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já cancelado");

        ap.setStatus(AppointmentStatus.CANCELLED);
        repository.save(ap);
    }

    @Transactional
    public DropoffAppointment completeDelivery(Long appointmentId, User collector, double weight) {

        DropoffAppointment ap = repository.findById(appointmentId).orElseThrow();

        if (ap.getStatus() != AppointmentStatus.CONFIRMED)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamento não confirmado");

        if (ap.isValidated())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entrega já validada");

        ap.setWeightKg(weight);
        ap.setValidated(true);
        ap.setValidatedBy(collector);
        ap.setValidatedAt(LocalDateTime.now());
        ap.setStatus(AppointmentStatus.COMPLETED);

        return repository.save(ap);
    }

    public List<DropoffAppointment> findByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<DropoffAppointment> findByCollectionPoint(Long pointId) {
        return repository.findByCollectionPointId(pointId);
    }
}