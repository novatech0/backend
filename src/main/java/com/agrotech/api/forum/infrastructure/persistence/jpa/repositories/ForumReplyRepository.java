package com.agrotech.api.forum.infrastructure.persistence.jpa.repositories;

import com.agrotech.api.forum.infrastructure.persistence.jpa.entities.ForumReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumReplyRepository extends JpaRepository<ForumReplyEntity, Long> {
    List<ForumReplyEntity> findAllByForumPost_Id(Long id);
}
