package com.agrotech.api.appointment.infrastructure.persistence.jpa.mappers;

import com.agrotech.api.appointment.domain.model.aggregates.Appointment;
import com.agrotech.api.appointment.domain.model.commands.CreateAppointmentCommand;
import com.agrotech.api.appointment.domain.model.entities.AvailableDate;
import com.agrotech.api.appointment.infrastructure.persistence.jpa.entities.AppointmentEntity;
import com.agrotech.api.profile.domain.model.entities.Farmer;

public class AppointmentMapper {
    public static Appointment toDomain(AppointmentEntity entity) {
        return new Appointment(
                entity.getId(),
                entity.getMessage(),
                entity.getStatus(),
                entity.getFarmer().getId(),
                entity.getAvailableDate().getId(),
                entity.getMeetingUrl()
        );
    }

    public static AppointmentEntity toEntity(CreateAppointmentCommand command, Farmer farmer, AvailableDate availableDate, String meetingUrl) {
        return new AppointmentEntity(command, meetingUrl, farmer, availableDate);
    }
}
