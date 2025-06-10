package com.sem.repository;

import com.sem.models.books.BookProfile;
import com.sem.models.books.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<BookProfile, Long> {
    Optional<BookProfile> findById(Long id);
    @Query("select book from BookDescription d where d.description like CONCAT('%', :desc, '%')")
    List<BookProfile> findByDescription(@Param("desc") String description);
    @Query("Select b from BookProfile b where b.name like CONCAT('%', :name, '%')")
    List<BookProfile> findByName(@Param("name") String description);
    List<BookProfile> findByGenre(Genre genre);
    @Query("select b from BookProfile b order by b.year desc limit :limit offset :offset")
    List<BookProfile> findNewest(@Param("limit") int limit, @Param("offset") int offset);
    @Query("SELECT b FROM BookProfile b " +
            "WHERE NOT EXISTS (" +
            "  SELECT 1 FROM UserProfile u JOIN u.booksRead br " +
            "  WHERE u.id = :id AND br.id = b.id" +
            ")")
    List<BookProfile> findByNotReadByUser(@Param("id") UUID id);
}
