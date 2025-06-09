package com.agrotech.api.appointment.domain.model.aggregates;

import com.agrotech.api.appointment.domain.model.commands.CreateAppointmentCommand;
import com.agrotech.api.appointment.domain.model.commands.UpdateAppointmentCommand;
import com.agrotech.api.appointment.domain.model.entities.AvailableDate;
import com.agrotech.api.appointment.domain.model.valueobjects.AppointmentStatus;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import lombok.Getter;

@Getter
public class Appointment {
    private Long id;
    private String message;
    private AppointmentStatus status;
    private String meetingUrl;
    private Farmer farmer;
    private AvailableDate availableDate;

    public Appointment() {}

    public Appointment(CreateAppointmentCommand command, String meetingUrl, Farmer farmer, AvailableDate availableDate) {
        this.message = command.message();
        this.status = AppointmentStatus.PENDING;
        this.meetingUrl = meetingUrl;
        this.farmer = farmer;
        this.availableDate = availableDate;
    }

    public Appointment(Long id, String message, AppointmentStatus status, String meetingUrl, Farmer farmer, AvailableDate availableDate) {
        this.id = id;
        this.message = message;
        this.status = status;
        this.meetingUrl = meetingUrl;
        this.farmer = farmer;
        this.availableDate = availableDate;
    }

    public Long getAvailableDateId() {
        return availableDate.getId();
    }

    public Long getFarmerId() {
        return farmer.getId();
    }

    public String getAppointmentStatus() {
        return status.name();
    }
}