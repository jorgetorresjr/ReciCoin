package org.ifpe.recicoin.entities.DTOs;

import java.time.LocalDateTime;

import org.ifpe.recicoin.entities.MaterialType;

public record AppointmentDTO(Long collectionPointId, LocalDateTime dateTime, String description, MaterialType materialType) {
}