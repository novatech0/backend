package com.agrotech.api.appointment.interfaces.rest.resources;

import java.time.LocalDate;

public record CreateAppointmentResource(Long advisorId,
                                        Long farmerId,
                                        String message,
                                        String status,
                                        LocalDate scheduledDate,
                                        String startTime,
                                        String endTime) {
}
