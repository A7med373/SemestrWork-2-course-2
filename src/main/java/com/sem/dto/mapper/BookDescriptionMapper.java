package com.sem.dto.mapper;

import com.sem.dto.BookDescriptionDTO;
import com.sem.models.books.BookDescription;

public class BookDescriptionMapper {
    public static BookDescriptionDTO toDto(BookDescription bookDescription){
        return new BookDescriptionDTO(bookDescription.getDescription());
    }
}
