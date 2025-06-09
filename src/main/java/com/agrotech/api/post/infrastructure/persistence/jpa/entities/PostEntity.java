package com.agrotech.api.post.infrastructure.persistence.jpa.entities;

import com.agrotech.api.profile.domain.model.entities.Advisor;
import com.agrotech.api.post.domain.model.commands.CreatePostCommand;
import com.agrotech.api.post.domain.model.commands.UpdatePostCommand;
import com.agrotech.api.shared.infrastructure.persistence.jpa.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor
public class PostEntity extends AuditableEntity {
    @NotNull(message = "Title is required")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description cannot be blank")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Image is required")
    private String image;

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    public PostEntity(CreatePostCommand command, Advisor advisor){
        this.title = command.title();
        this.description = command.description();
        this.image = command.image();
        this.advisor = advisor;
    }

    public PostEntity update(UpdatePostCommand command){
        this.title = command.title();
        this.description = command.description();
        this.image = command.image();
        return this;
    }

    public Long getAdvisorId(){
        return this.advisor.getId();
    }

}
