package org.ifpe.recicoin.entities;

public enum AppointmentStatus {
    SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, MISSED;
    // SCHEDULED Criado pelo usuário (Aguardando empresa aceitar)
    // CONFIRMED - Empresa aceitou (Aguardando cliente entregar)
    // COMPLETED - Entregue e finalizado (Pontos creditados)
    // CANCELLED - Recusado ou Cliente não foi
}
