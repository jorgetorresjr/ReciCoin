package org.ifpe.recicoin.entities;

import java.time.LocalDateTime;

public record AppointmentDTO(Long collectionPointId, LocalDateTime dateTime, String description) {
}