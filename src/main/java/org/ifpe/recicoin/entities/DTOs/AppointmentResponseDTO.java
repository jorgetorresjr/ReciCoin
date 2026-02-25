package org.ifpe.recicoin.entities.DTOs;

import org.ifpe.recicoin.entities.enums.AppointmentStatus;
import org.ifpe.recicoin.entities.enums.MaterialType;

import java.time.LocalDateTime;

public record AppointmentResponseDTO(Long id, Long userId, Long collectionPointId, MaterialType materialType, String description, LocalDateTime scheduledTime, AppointmentStatus status, Double weightKg, Boolean validated) {
}
