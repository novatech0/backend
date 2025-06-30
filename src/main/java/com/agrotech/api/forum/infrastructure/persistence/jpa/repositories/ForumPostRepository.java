package com.agrotech.api.forum.infrastructure.persistence.jpa.repositories;

import com.agrotech.api.forum.infrastructure.persistence.jpa.entities.ForumPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ForumPostRepository extends JpaRepository<ForumPostEntity, Long> {
}
