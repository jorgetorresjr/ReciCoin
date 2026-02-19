package org.ifpe.recicoin.entities.DTOs;

import java.time.LocalDateTime;

public record AppointmentDTO(Long collectionPointId, LocalDateTime dateTime, String description) {
}