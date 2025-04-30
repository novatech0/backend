package com.agrotech.api.appointment.domain.model.queries;

import java.time.LocalDate;

public record GetAvailableDateByAdvisorIdAndDate(
        Long advisorId,
        LocalDate availableDate,
        String startTime,
        String endTime
) {
}
