package com.sem.repository;

import com.sem.models.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    @Query("Select u from userProfile u where u.id = :userId")
    Optional<UserProfile> findById (@Param("userId") UUID userId);
}
