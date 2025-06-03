package com.sem.dto;

import com.sem.models.books.BookProfile;
import com.sem.models.books.Genre;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank @Size(max = 100)
    private String author;

    private Genre genre;

    private Date publicationYear;

}
