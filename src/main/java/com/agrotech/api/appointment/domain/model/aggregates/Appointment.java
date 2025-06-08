package com.agrotech.api.appointment.domain.model.aggregates;

import com.agrotech.api.appointment.domain.model.valueobjects.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private Long id;
    private String message;
    private AppointmentStatus status;
    private Long farmerId;
    private Long availableDateId;
    private String meetingUrl;

    public String getAppointmentStatus() {
        return status.toString();
    }
}
