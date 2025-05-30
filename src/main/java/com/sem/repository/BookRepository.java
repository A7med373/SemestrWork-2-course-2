package com.sem.repository;

import com.sem.models.books.BookProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookProfile, Long> {
    @Query("Select b from bookProfile b Where b.id = :id")
    Optional<BookProfile> findById(@Param("id") Long id);
    @Query("select book from bookDescription d where d.description like CONCAT('%', :desc, '%')")
    List<BookProfile> findByDescription(@Param("desc") String description);
    @Query("Select b from bookProfile b where b.name like CONCAT('%', :name, '%')")
    List<BookProfile> findByName(@Param("name") String description);
}
