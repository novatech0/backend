package com.agrotech.api.forum.infrastructure.persistence.jpa.entities;

import com.agrotech.api.iam.infrastructure.persistence.jpa.entities.UserEntity;
import com.agrotech.api.shared.infrastructure.persistence.jpa.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "forum_reply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumReplyEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "forum_post_id")
    private ForumPostEntity forumPost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}