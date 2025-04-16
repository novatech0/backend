package com.agrotech.api.management.domain.model.entities;

import com.agrotech.api.management.domain.model.aggregates.Enclosure;
import com.agrotech.api.management.domain.model.commands.CreateAnimalCommand;
import com.agrotech.api.management.domain.model.commands.UpdateAnimalCommand;
import com.agrotech.api.management.domain.model.valueobjects.HealthStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String species;
    private String breed;
    private Boolean gender;
    private Float weight;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Health status is required")
    private HealthStatus health;

    @ManyToOne
    @JoinColumn(name = "enclosure_id")
    private Enclosure enclosure;

    public Animal() {}

    public Animal(CreateAnimalCommand command, Enclosure enclosure) {
        this.name = command.name();
        this.age = command.age();
        this.species = command.species();
        this.breed = command.breed();
        this.gender = command.gender();
        this.weight = command.weight();
        this.health = HealthStatus.valueOf(command.health().toUpperCase());
        this.enclosure = enclosure;
    }

    public Animal update(UpdateAnimalCommand command) {
        this.name = command.name();
        this.age = command.age();
        this.species = command.species();
        this.breed = command.breed();
        this.gender = command.gender();
        this.weight = command.weight();
        this.health = HealthStatus.valueOf(command.health().toUpperCase());
        return this;
    }

    public Long getEnclosureId() {
        return this.enclosure.getId();
    }

    public String getHealthStatus() {
        return this.health.toString();
    }
}
