package com.agrotech.api.management.domain.model.aggregates;

import com.agrotech.api.management.domain.model.commands.CreateEnclosureCommand;
import com.agrotech.api.management.domain.model.commands.UpdateEnclosureCommand;
import com.agrotech.api.management.domain.model.entities.Animal;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import com.agrotech.api.shared.infrastructure.persistence.jpa.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Enclosure extends AuditableEntity {
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;
    @NotNull(message = "Type is required")
    @NotBlank(message = "Type cannot be blank")
    private String type;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @OneToMany(mappedBy = "enclosure", cascade = CascadeType.ALL)
    private List<Animal> animals;

    public Enclosure() {}

    public Enclosure(CreateEnclosureCommand command, Farmer farmer) {
        this.name = command.name();
        this.capacity = command.capacity();
        this.type = command.type();
        this.farmer = farmer;
        this.animals = new ArrayList<>();
    }

    public Enclosure update(UpdateEnclosureCommand command) {
        this.name = command.name();
        this.capacity = command.capacity();
        this.type = command.type();
        return this;
    }

    public Long getFarmerId() {
        return this.farmer.getId();
    }
}