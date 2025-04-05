package com.agrotech.api.appointment.domain.model.commands;

import java.time.LocalDate;

public record CreateAppointmentCommand(Long advisorId,
                                       Long farmerId,
                                       String message,
                                       String status,
                                       LocalDate scheduledDate,
                                       String startTime,
                                       String endTime) {
}
