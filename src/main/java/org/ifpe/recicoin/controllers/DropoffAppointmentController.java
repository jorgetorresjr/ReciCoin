package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.*;
import org.ifpe.recicoin.entities.DTOs.AppointmentDTO;
import org.ifpe.recicoin.entities.enums.AppointmentStatus;
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
    private org.ifpe.recicoin.service.DeliveryApplicationService deliveryService;
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
            appt.setMaterialType(dto.materialType());

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
            
            // HACK: Passamos o ID do próprio dono do resíduo como "validador" 
            // para o serviço do Jorge não travar (já que o Ponto de Coleta não é da classe User)
            deliveryService.confirmDelivery(id, appt.getUser().getId(), weightKg);
            
            // Atualiza o status final
            appt.setStatus(AppointmentStatus.COMPLETED);
            appointmentRepository.save(appt);

            return ResponseEntity.ok("Entrega confirmada e pontos gerados!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}