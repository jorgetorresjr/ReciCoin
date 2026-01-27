package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.*;
import org.ifpe.recicoin.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.ifpe.recicoin.entities.AppointmentStatus;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class DropoffAppointmentController {

    @Autowired
    private DropoffAppointmentRepository appointmentRepository;
    @Autowired
    private CollectionPointRepository pointRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO dto) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CollectionPoint point = pointRepository.findById(dto.collectionPointId())
                .orElseThrow(() -> new RuntimeException("Ponto não encontrado"));

        DropoffAppointment appointment = new DropoffAppointment();
        appointment.setUser(user);
        appointment.setCollectionPoint(point);
        appointment.setScheduledDateTime(dto.dateTime());
        appointment.setStatus(AppointmentStatus.SCHEDULED); 

        appointmentRepository.save(appointment);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-schedule")
    public ResponseEntity<List<DropoffAppointment>> getPointAppointments() {

        CollectionPoint point = (CollectionPoint) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<DropoffAppointment> list = appointmentRepository.findByCollectionPoint(point);
        return ResponseEntity.ok(list);
    }
}