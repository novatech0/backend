package com.agrotech.api.post.domain.model.aggregates;

import com.agrotech.api.profile.domain.model.entities.Advisor;
import com.agrotech.api.post.domain.model.commands.CreatePostCommand;
import com.agrotech.api.post.domain.model.commands.UpdatePostCommand;
import com.agrotech.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class Post extends AuditableAbstractAggregateRoot<Post> {
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

    public Post() {
    }

    public Post(CreatePostCommand command, Advisor advisor){
        this.title = command.title();
        this.description = command.description();
        this.image = command.image();
        this.advisor = advisor;
    }

    public Post update(UpdatePostCommand command){
        this.title = command.title();
        this.description = command.description();
        this.image = command.image();
        return this;
    }

    public Long getAdvisorId(){
        return this.advisor.getId();
    }

}
