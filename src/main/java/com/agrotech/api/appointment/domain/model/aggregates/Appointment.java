package com.agrotech.api.appointment.domain.model.aggregates;

import com.agrotech.api.appointment.domain.model.commands.CreateAppointmentCommand;
import com.agrotech.api.appointment.domain.model.commands.UpdateAppointmentCommand;
import com.agrotech.api.appointment.domain.model.entities.AvailableDate;
import com.agrotech.api.appointment.domain.model.valueobjects.AppointmentStatus;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import com.agrotech.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Entity
public class Appointment extends AuditableAbstractAggregateRoot<Appointment> {
    @NotNull(message = "Message is required")
    @Column(columnDefinition = "TEXT")
    private String message;

    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @OneToOne
    @JoinColumn(name = "available_date_id")
    private AvailableDate availableDate;

    private String meetingUrl;

    public Appointment() {
    }

    public Appointment(CreateAppointmentCommand command, String meetingUrl, Farmer farmer, AvailableDate availableDate) {
        this.message = command.message();
        this.status = AppointmentStatus.PENDING;
        this.farmer = farmer;
        this.availableDate = availableDate;
        this.meetingUrl = meetingUrl;
    }

    public Appointment update(UpdateAppointmentCommand command){
        this.message = command.message();
        this.status = AppointmentStatus.valueOf(command.status());
        return this;
    }

    public Long getAvailableDateId() {
        return availableDate.getId();
    }

    public Long getFarmerId() {
        return farmer.getId();
    }

    public String getAppointmentStatus() {
        return status.toString();
    }
}
