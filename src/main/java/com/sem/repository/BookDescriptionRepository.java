package com.sem.repository;

import com.sem.models.books.BookDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookDescriptionRepository extends JpaRepository<BookDescription, Long> {
    @Query("select d from BookDescription d where d.description like CONCAT('%', :desc, '%')")
    List<BookDescription> findByDescription(@Param("desc") String description);
    @Query("select d from BookDescription d where d.id = :id")
    List<BookDescription> findDescriptionById(@Param("id") String description);
}
