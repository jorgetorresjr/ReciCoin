package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.*;
import org.ifpe.recicoin.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class DropoffAppointmentController {

    @Autowired
    private DropoffAppointmentRepository appointmentRepository;
    @Autowired
    private CollectionPointRepository pointRepository;
    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO dto) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof User)) return ResponseEntity.status(403).body("Apenas usuários.");

            CollectionPoint point = pointRepository.findById(dto.collectionPointId())
                    .orElseThrow(() -> new RuntimeException("Ponto não encontrado."));

            DropoffAppointment appt = new DropoffAppointment();
            appt.setUser((User) principal);
            appt.setCollectionPoint(point);
            appt.setScheduledDateTime(dto.dateTime());
            appt.setDescription(dto.description());
            appt.setStatus(AppointmentStatus.SCHEDULED);

            appointmentRepository.save(appt);
            return ResponseEntity.ok("Agendado! Aguardando aprovação do ponto.");
        } catch (Exception e) { return ResponseEntity.badRequest().body("Erro: " + e.getMessage()); }
    }
    @GetMapping("/my-appointments")
    public ResponseEntity<List<DropoffAppointment>> getUserAppointments() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) return ResponseEntity.ok(appointmentRepository.findByUser((User) principal));
        return ResponseEntity.status(403).build();
    }
    @GetMapping("/my-schedule")
    public ResponseEntity<List<DropoffAppointment>> getPointAppointments() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CollectionPoint) return ResponseEntity.ok(appointmentRepository.findByCollectionPoint((CollectionPoint) principal));
        return ResponseEntity.status(403).build();
    }
    @PostMapping("/{id}/accept")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        return updateStatus(id, AppointmentStatus.CONFIRMED);
    }
   @PostMapping("/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        return updateStatus(id, AppointmentStatus.COMPLETED);
    }
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        return updateStatus(id, AppointmentStatus.CANCELLED);
    }

    private ResponseEntity<?> updateStatus(Long id, AppointmentStatus status) {
        DropoffAppointment appt = appointmentRepository.findById(id).orElseThrow();
        appt.setStatus(status);
        appointmentRepository.save(appt);
        return ResponseEntity.ok().build();
    }
}