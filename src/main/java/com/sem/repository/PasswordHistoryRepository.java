package com.sem.repository;

import com.sem.models.user.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    @Query("Select p from passwordHistory p where userId = :userId")
    List<PasswordHistory> findLastByUserIdOrderByCreatedAtDesc(@Param("userId") UUID userId);
}
