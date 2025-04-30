package com.agrotech.api.appointment.interfaces.rest.resources;

import java.time.LocalDate;

public record CreateAvailableDateResource(Long advisorId,
                                          LocalDate availableDate,
                                          String startTime,
                                          String endTime) {
}
