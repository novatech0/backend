package com.agrotech.api.profile.domain.model.aggregates;

import com.agrotech.api.iam.domain.model.aggregates.User;
import com.agrotech.api.profile.domain.model.commands.CreateProfileCommand;
import com.agrotech.api.profile.domain.model.commands.UpdateProfileCommand;
import com.agrotech.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
public class Profile extends AuditableAbstractAggregateRoot<Profile> {
    @NotNull(message = "First Name is required")
    @NotBlank(message = "First Name cannot be blank")
    private String firstName;
    @NotNull(message = "Last Name is required")
    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;
    @NotNull(message = "City is required")
    private String city;
    @NotNull(message = "Country is required")
    private String country;
    @NotNull(message = "Birth Date is required")
    @Past(message = "Birth Date must be in the past")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
    private String description;
    private String photo;
    private String occupation;
    @Min(value = 0, message = "Experience must be greater than or equal to 0")
    private Integer experience;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Profile() {
    }

    public Profile(CreateProfileCommand command, User user){
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.city = command.city();
        this.country = command.country();
        this.birthDate = command.birthDate();
        this.description = command.description();
        this.photo = command.photo();
        this.occupation = command.occupation();
        this.experience = command.experience();
        this.user = user;
    }

    public Profile update(UpdateProfileCommand command){
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.city = command.city();
        this.country = command.country();
        this.birthDate = command.birthDate();
        this.description = command.description();
        this.photo = command.photo();
        this.occupation = command.occupation();
        this.experience = command.experience();
        return this;
    }

    public Long getUserId() {
        return this.user.getId();
    }


}
