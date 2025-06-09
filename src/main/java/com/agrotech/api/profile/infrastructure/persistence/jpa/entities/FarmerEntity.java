package com.agrotech.api.profile.infrastructure.persistence.jpa.entities;

import com.agrotech.api.iam.infrastructure.persistence.jpa.entities.UserEntity;
import com.agrotech.api.shared.infrastructure.persistence.jpa.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "farmer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmerEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
