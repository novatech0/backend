package com.agrotech.api.management.domain.model.aggregates;

import com.agrotech.api.management.domain.model.commands.CreateEnclosureCommand;
import com.agrotech.api.management.domain.model.commands.UpdateEnclosureCommand;
import com.agrotech.api.management.domain.model.entities.Animal;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import com.agrotech.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Enclosure extends AuditableAbstractAggregateRoot<Enclosure> {
    private String name;
    private Integer capacity;
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