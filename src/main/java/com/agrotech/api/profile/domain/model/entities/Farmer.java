package com.agrotech.api.profile.domain.model.entities;

import com.agrotech.api.iam.domain.model.aggregates.User;
import com.agrotech.api.profile.domain.model.commands.CreateFarmerCommand;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Farmer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Farmer() {
    }

    public Farmer(CreateFarmerCommand command, User user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getId();
    }
}
