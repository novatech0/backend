package com.agrotech.api.forum.infrastructure.persistence.jpa.repositories;

import com.agrotech.api.forum.infrastructure.persistence.jpa.entities.ForumFavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ForumFavoriteRepository extends JpaRepository<ForumFavoriteEntity, Long> {
    Optional<ForumFavoriteEntity> findByUser_IdAndForumPost_Id(Long userId, Long forumPostId);
    List<ForumFavoriteEntity> findAllByForumPost_Id(Long forumPostId);
}
