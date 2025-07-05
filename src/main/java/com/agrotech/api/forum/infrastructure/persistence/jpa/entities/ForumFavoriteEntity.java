package com.agrotech.api.forum.infrastructure.persistence.jpa.entities;

import com.agrotech.api.iam.infrastructure.persistence.jpa.entities.UserEntity;
import com.agrotech.api.shared.infrastructure.persistence.jpa.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "forum_favorite", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "forum_post_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumFavoriteEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "forum_post_id", nullable = false)
    private ForumPostEntity forumPost;
}
