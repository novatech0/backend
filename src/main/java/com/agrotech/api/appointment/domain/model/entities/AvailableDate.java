package com.agrotech.api.appointment.domain.model.entities;

import com.agrotech.api.appointment.domain.model.commands.CreateAvailableDateCommand;
import com.agrotech.api.appointment.domain.model.commands.UpdateAvailableDateCommand;
import com.agrotech.api.appointment.domain.model.valueobjects.AvailableDateStatus;
import com.agrotech.api.profile.domain.model.entities.Advisor;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
public class AvailableDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    @NotNull(message = "Date is required")
    private LocalDate scheduledDate;

    @NotNull(message = "Starting time is required")
    @JsonFormat(pattern = "HH:mm:ss")
    private String startTime;

    @NotNull(message = "Ending time is required")
    @JsonFormat(pattern = "HH:mm:ss")
    private String endTime;

    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
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
    public String getAvailableDateStatus() { return status.toString(); }
}
