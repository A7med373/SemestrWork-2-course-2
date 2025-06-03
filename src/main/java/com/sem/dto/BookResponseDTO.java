package com.sem.dto;

import com.sem.models.books.BookProfile;
import com.sem.models.books.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private Long id;
    private String name;
    private String author;
    private Genre genre;
    private Date year;
    private BookDescriptionDTO description;
}
