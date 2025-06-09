package com.agrotech.api.appointment.domain.model.entities;

import com.agrotech.api.appointment.domain.model.commands.CreateAvailableDateCommand;
import com.agrotech.api.appointment.domain.model.commands.UpdateAvailableDateCommand;
import com.agrotech.api.appointment.domain.model.valueobjects.AvailableDateStatus;
import com.agrotech.api.profile.domain.model.entities.Advisor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AvailableDate {
    private Long id;
    private Advisor advisor;
    private LocalDate scheduledDate;
    private String startTime;
    private String endTime;
    private AvailableDateStatus status;

    public AvailableDate() {
    }

    public AvailableDate(CreateAvailableDateCommand command, Advisor advisor) {
        this.scheduledDate = command.scheduledDate();
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        this.advisor = advisor;
        this.status = AvailableDateStatus.AVAILABLE;
    }

    public AvailableDate(Long id, LocalDate scheduledDate, String startTime, String endTime, AvailableDateStatus status, Advisor advisor) {
        this.id = id;
        this.scheduledDate = scheduledDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.advisor = advisor;
    }

    public AvailableDate update(UpdateAvailableDateCommand command) {
        this.scheduledDate = command.scheduledDate();
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        return this;
    }

    public AvailableDate updateStatus(String status) {
        this.status = AvailableDateStatus.valueOf(status);
        return this;
    }

    public Long getAdvisorId() {
        return advisor.getId();
    }

    public String getAvailableDateStatus() {
        return status.name();
    }
}