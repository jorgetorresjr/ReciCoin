package org.ifpe.recicoin.mapper;

import org.ifpe.recicoin.entities.DropoffAppointment;
import org.ifpe.recicoin.entities.DTOs.AppointmentResponseDTO;

public class DropoffAppointmentMapper {
    private DropoffAppointmentMapper() {}

    public static AppointmentResponseDTO toDTO(DropoffAppointment ap) {
        if (ap == null) return null;

        return new AppointmentResponseDTO(
                ap.getId(),
                ap.getUser() != null ? ap.getUser().getId() : null,
                ap.getCollectionPoint() != null ? ap.getCollectionPoint().getId() : null,
                ap.getMaterialType(),
                ap.getDescription(),
                ap.getScheduledDateTime(),
                ap.getStatus(),
                ap.getWeightKg(),
                ap.isValidated()
        );
    }
}