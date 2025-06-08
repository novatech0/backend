package com.agrotech.api.appointment.infrastructure.persistence.jpa.entities;

import com.agrotech.api.appointment.domain.model.commands.CreateAppointmentCommand;
import com.agrotech.api.appointment.domain.model.commands.UpdateAppointmentCommand;
import com.agrotech.api.appointment.domain.model.entities.AvailableDate;
import com.agrotech.api.appointment.domain.model.valueobjects.AppointmentStatus;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import com.agrotech.api.shared.infrastructure.persistence.jpa.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "appointment")
@NoArgsConstructor
public class AppointmentEntity extends AuditableEntity {
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

    public AppointmentEntity(CreateAppointmentCommand command, String meetingUrl, Farmer farmer, AvailableDate availableDate) {
        this.message = command.message();
        this.status = AppointmentStatus.PENDING;
        this.farmer = farmer;
        this.availableDate = availableDate;
        this.meetingUrl = meetingUrl;
    }

    public AppointmentEntity update(UpdateAppointmentCommand command){
        this.message = command.message();
        this.status = AppointmentStatus.valueOf(command.status());
        return this;
    }

    public AppointmentEntity updateStatus(AppointmentStatus status){
        this.status = status;
        return this;
    }

    public Long getAvailableDateId() {
        return availableDate.getId();
    }

    public Long getFarmerId() {
        return farmer.getId();
    }
}
