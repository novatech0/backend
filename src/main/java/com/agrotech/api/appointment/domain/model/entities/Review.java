package com.agrotech.api.appointment.domain.model.entities;

import com.agrotech.api.appointment.domain.model.commands.CreateReviewCommand;
import com.agrotech.api.appointment.domain.model.commands.UpdateReviewCommand;
import com.agrotech.api.profile.domain.model.entities.Advisor;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Min(value = 0, message = "Rating must be between 0 and 5")
    @Max(value = 5, message = "Rating must be between 0 and 5")
    @NotNull(message = "Rating is required")
    private Integer rating;

    public Review() {
    }

    public Review(CreateReviewCommand command, Advisor advisor, Farmer farmer) {
        this.comment = command.comment();
        this.rating = command.rating();
        this.advisor = advisor;
        this.farmer = farmer;
    }

    public Review update(UpdateReviewCommand command) {
        this.comment = command.comment();
        this.rating = command.rating();
        return this;
    }

    public Long getAdvisorId() {
        return advisor.getId();
    }
    public Long getFarmerId() { return farmer.getId(); }
}
