package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.*;
import org.ifpe.recicoin.entities.DTOs.AppointmentDTO;
import org.ifpe.recicoin.entities.DTOs.AppointmentResponseDTO;
import org.ifpe.recicoin.mapper.DropoffAppointmentMapper;
import org.ifpe.recicoin.service.DeliveryApplicationService;
import org.ifpe.recicoin.service.DropoffAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class DropoffAppointmentController {

    @Autowired
    private DeliveryApplicationService deliveryService;

    @Autowired
    private DropoffAppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO dto) {
        try {
            User user = getAuthenticatedUser();

            DropoffAppointment created = appointmentService.schedule(
                    user.getId(),
                    dto.collectionPointId(),
                    dto.dateTime(),
                    dto.description(),
                    dto.materialType()
            );

            return ResponseEntity.ok(DropoffAppointmentMapper.toDTO(created));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getUserAppointments() {

        User user = getAuthenticatedUser();

        List<AppointmentResponseDTO> response =
                appointmentService.findByUser(user.getId())
                        .stream()
                        .map(DropoffAppointmentMapper::toDTO)
                        .toList();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/my-schedule")
    public ResponseEntity<List<DropoffAppointment>> getPointAppointments() {
        CollectionPoint point = getAuthenticatedPoint();
        return ResponseEntity.ok(appointmentService.findByCollectionPoint(point.getId()));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        CollectionPoint point = getAuthenticatedPoint();
        appointmentService.accept(id, point.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        User user = getAuthenticatedUser();
        appointmentService.cancel(id, user.getId());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeAppointment(@PathVariable Long id,
                                                 @RequestParam Double weightKg) {
        try {
            User collector = getAuthenticatedUser();
            deliveryService.confirmDelivery(id, collector.getId(), weightKg);
            return ResponseEntity.ok("Entrega confirmada e pontos gerados!");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof User user))
            throw new RuntimeException("Usuário não autenticado");
        return user;
    }

    private CollectionPoint getAuthenticatedPoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CollectionPoint point))
            throw new RuntimeException("Ponto de coleta não autenticado");
        return point;
    }
}