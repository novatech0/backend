package com.agrotech.api.appointment.interfaces.rest.resources;

import java.time.LocalDate;

public record AppointmentResource(Long id,
                                  Long advisorId,
                                  Long farmerId,
                                  String message,
                                  String status,
                                  LocalDate scheduledDate,
                                  String startTime,
                                  String endTime,
                                  String meetingUrl) {
}
