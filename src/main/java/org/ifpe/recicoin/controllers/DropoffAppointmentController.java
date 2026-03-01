package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.*;
import org.ifpe.recicoin.entities.DTOs.AppointmentDTO;
import org.ifpe.recicoin.entities.enums.AppointmentStatus;
import org.ifpe.recicoin.repositories.*;
import org.ifpe.recicoin.service.DeliveryApplicationService;
import org.ifpe.recicoin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class DropoffAppointmentController {

    @Autowired
    private DeliveryApplicationService deliveryService;

    @Autowired
    private DropoffAppointmentRepository appointmentRepository;

    @Autowired
    private CollectionPointRepository pointRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO dto) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body("Apenas usuários podem criar agendamento.");
            }

            User user = (User) principal;

            CollectionPoint point = pointRepository.findById(dto.collectionPointId())
                    .orElseThrow(() -> new RuntimeException("Ponto não encontrado."));

            DropoffAppointment appt = new DropoffAppointment();
            appt.setUser(user);
            appt.setCollectionPoint(point);
            appt.setScheduledDateTime(dto.dateTime());
            appt.setDescription(dto.description());
            appt.setMaterialType(dto.materialType());
            appt.setStatus(AppointmentStatus.SCHEDULED);

            appointmentRepository.save(appt);
            return ResponseEntity.ok("Agendado! Aguardando aprovação do ponto.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<DropoffAppointment>> getUserAppointments() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User)) {
            return ResponseEntity.status(403).body(null);
        }

        User user = (User) principal;

        List<DropoffAppointment> appointments = appointmentRepository.findByUserId(user.getId());
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/my-schedule")
    public ResponseEntity<List<DropoffAppointment>> getPointAppointments() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Principal: " + principal);
        
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CollectionPoint point = pointRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Ponto não encontrado"));
        return ResponseEntity.ok(appointmentRepository.findByCollectionPoint(point));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        return updateStatus(id, AppointmentStatus.CONFIRMED);
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

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeAppointment(@PathVariable Long id, @RequestParam Double weightKg) {
        try {
            DropoffAppointment appt = appointmentRepository.findById(id).orElseThrow();

            deliveryService.confirmDelivery(id, appt.getUser().getId(), weightKg);

            appt.setStatus(AppointmentStatus.COMPLETED);
            appointmentRepository.save(appt);

            return ResponseEntity.ok("Entrega confirmada e pontos gerados!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}