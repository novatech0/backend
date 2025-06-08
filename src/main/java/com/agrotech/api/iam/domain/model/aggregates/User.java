package com.agrotech.api.iam.domain.model.aggregates;

import com.agrotech.api.iam.domain.model.entities.Role;
import com.agrotech.api.profile.domain.model.entities.Notification;
import com.agrotech.api.shared.infrastructure.persistence.jpa.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User aggregate root
 * This class represents the aggregate root for the User entity.
 *
 * @see AuditableEntity
 */
@Getter
@Setter
@Entity
public class User extends AuditableEntity {

    @NotBlank(message = "Username is required")
    @Email(message = "Username must be an email")
    @Size(max = 50, message = "Username must be less than 50 characters")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max = 120, message = "Password must be less than 120 characters")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    public User() {
        this.roles = new HashSet<>();
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
        this.notifications = new ArrayList<>();
    }

    public User(String username, String password, List<Role> roles) {
        this(username, password);
        addRoles(roles);
    }

    /**
     * Add a role to the user
     * @param role the role to add
     * @return the user with the added role
     */
    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    /**
     * Add a list of roles to the user
     * @param roles the list of roles to add
     * @return the user with the added roles
     */
    public User addRoles(List<Role> roles) {
        var validatedRoleSet = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoleSet);
        return this;
    }

}