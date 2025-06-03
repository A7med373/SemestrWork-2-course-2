package com.sem.dto.mapper;

import com.sem.dto.BookResponseDTO;
import com.sem.models.books.BookProfile;

public class BookMapper {
    public static BookResponseDTO toDto(BookProfile book){
        new BookResponseDTO();
        return BookResponseDTO.builder().
                id(book.getId()).
                name(book.getName()).
                author(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName()).
                genre(book.getGenre()).
                year(book.getYear()).
                description(BookDescriptionMapper.toDto(book.getBookDescription())).
                build();
    }
}
