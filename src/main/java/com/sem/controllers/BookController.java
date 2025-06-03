package com.sem.controllers;
//
//import com.sem.dto.BookResponseDTO;
//import com.sem.dto.mapper.BookMapper;
//import com.sem.service.BookProfileService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/books")
//@RequiredArgsConstructor
//public class BookController {
//
//    private final BookProfileService bookService;
//
//    // Получение книги по ID
//    @GetMapping("/books/{id}")
//    public ResponseEntity<BookResponseDTO> getById(@PathVariable Long id) {
//        return bookService.findById(id)
//                .map(BookMapper::toDto)
//                .map(book -> ResponseEntity.ok(book))
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//
//    // Получение самых новых книг
//    @GetMapping("/books/newest")
//    public ResponseEntity<BookResponseDTO> getNewest() {
//        return null;
//    }
//
//    // Поиск книг по автору
//    @GetMapping("/search/author")
//    public ResponseEntity<PaginatedResponseDTO<BookResponseDTO>> getByAuthor(
//            @RequestParam String author,
//            @PageableDefault(size = 15) Pageable pageable) {
//
//        Page<BookResponseDTO> page = bookService.findByAuthor(author, pageable)
//                .map(bookMapper::toDto);
//
//        return ResponseEntity.ok(new PaginatedResponseDTO<>(
//                page.getContent(),
//                page.getNumber(),
//                page.getTotalPages(),
//                page.getTotalElements()
//        ));
//    }
//
//    // Поиск книг по названию
//    @GetMapping("/search/title")
//    public ResponseEntity<PaginatedResponseDTO<BookResponseDTO>> getByName(
//            @RequestParam String title,
//            @PageableDefault(size = 15) Pageable pageable) {
//
//        Page<BookResponseDTO> page = bookService.findByTitleContaining(title, pageable)
//                .map(bookMapper::toDto);
//
//        return ResponseEntity.ok(new PaginatedResponseDTO<>(
//                page.getContent(),
//                page.getNumber(),
//                page.getTotalPages(),
//                page.getTotalElements()
//        ));
//    }
//
//    // Поиск книг по описанию
//    @GetMapping("/search/description")
//    public ResponseEntity<PaginatedResponseDTO<BookResponseDTO>> getByDescription(
//            @RequestParam String keyword,
//            @PageableDefault(size = 15) Pageable pageable) {
//
//        Page<BookResponseDTO> page = bookService.findByDescriptionContaining(keyword, pageable)
//                .map(bookMapper::toDto);
//
//        return ResponseEntity.ok(new PaginatedResponseDTO<>(
//                page.getContent(),
//                page.getNumber(),
//                page.getTotalPages(),
//                page.getTotalElements()
//        ));
//    }
//}
