package com.agrotech.api.appointment.domain.model.entities;

import com.agrotech.api.appointment.domain.model.commands.CreateAvailableDateCommand;
import com.agrotech.api.appointment.domain.model.commands.UpdateAvailableDateCommand;
import com.agrotech.api.profile.domain.model.entities.Advisor;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
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

    @NotNull(message = "Available Date is required")
    @FutureOrPresent(message = "Available Date must be in the future")
    private LocalDate availableDate;

    @NotNull(message = "Starting time is required")
    @JsonFormat(pattern = "HH:mm:ss")
    private String startTime;

    @NotNull(message = "Ending time is required")
    @JsonFormat(pattern = "HH:mm:ss")
    private String endTime;

    public AvailableDate() {
    }

    public AvailableDate(CreateAvailableDateCommand command, Advisor advisor) {
        this.availableDate = command.availableDate();
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        this.advisor = advisor;
    }

    public AvailableDate update(UpdateAvailableDateCommand command) {
        this.availableDate = command.availableDate();
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        return this;
    }

    public Long getAdvisorId() {
        return advisor.getId();
    }
}
