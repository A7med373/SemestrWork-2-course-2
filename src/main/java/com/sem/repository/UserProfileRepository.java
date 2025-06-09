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
    Optional<UserProfile> findById (UUID userId);
    Optional<UserProfile> findByEmail(String email);
    @Query("SELECT u FROM UserProfile u WHERE CONCAT(u.firstName, ' ', u.lastName) LIKE CONCAT('%', :name, '%')")
    List<UserProfile> findByName(@Param("name") String name);
}
