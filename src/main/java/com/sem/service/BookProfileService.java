package com.sem.service;

import com.sem.models.books.BookDescription;
import com.sem.models.books.BookProfile;
import com.sem.models.user.UserProfile;
import com.sem.repository.BookDescriptionRepository;
import com.sem.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookProfileService {

    private final BookRepository bookProfileRepository;
    private final AuthorizationService authorizationService;
    private final BookDescriptionRepository bookDescriptionRepository;


    @Transactional(readOnly = true)
    public BookProfile getBookById(Long id) {
        return bookProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    @Transactional
    public BookProfile createBook(BookProfile book, UUID authorId) {
        // Проверка прав доступа
        if (!authorizationService.hasAccess(authorId, "AUTHOR")) {
            throw new AccessDeniedException("Only authors can create books");
        }

        // Связывание с автором
        UserProfile author = new UserProfile();
        author.setId(authorId);
        book.setAuthor(author);

        return bookProfileRepository.save(book);
    }

    @Transactional
    public BookProfile updateBookDescription(Long bookId, String description) {
        BookProfile book = getBookById(bookId);

        // Проверка прав (автор или админ)
        UUID authorId = book.getAuthor().getId();
        if (!authorizationService.hasAccess(authorId, "ADMIN") &&
                !authorizationService.hasAccess(authorId, null)) {
            throw new AccessDeniedException("Only author or admin can update book");
        }

        BookDescription bookDescription = book.getBookDescriptionId();
        bookDescription.setDescription(description);
        bookDescriptionRepository.save(bookDescription);
        return book;
    }

    @Transactional
    public void deleteBook(Long bookId) {
        BookProfile book = getBookById(bookId);

        // Проверка прав (автор или админ)
        UUID authorId = book.getAuthor().getId();
        if (!authorizationService.hasAccess(authorId, "ADMIN") &&
                !authorizationService.hasAccess(authorId, null)) {
            throw new AccessDeniedException("Only author or admin can delete book");
        }

        bookProfileRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public List<BookProfile> searchBooksByDescription(String query) {
        return bookProfileRepository.findByDescription(query);
    }
}