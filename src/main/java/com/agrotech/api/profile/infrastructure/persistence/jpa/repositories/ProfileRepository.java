package com.agrotech.api.profile.infrastructure.persistence.jpa.repositories;

import com.agrotech.api.profile.domain.model.aggregates.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser_Id(Long userId);
    @Query("SELECT p FROM Profile p WHERE p.occupation IS NOT NULL AND p.occupation <> ''")
    List<Profile> findAllAdvisorProfiles();
}
