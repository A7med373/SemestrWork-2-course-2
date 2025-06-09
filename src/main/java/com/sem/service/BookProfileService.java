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

    @Transactional(readOnly = true)
    public List<BookProfile> findNewestBooks(int quantity, int offset){
        return bookProfileRepository.findNewest(quantity, offset);
    }

    @Transactional
    public BookProfile createBook(BookProfile book, UUID authorId) {
        if (!authorizationService.hasAccess(authorId, "AUTHOR")) {
            throw new AccessDeniedException("Only authors can create books");
        }

        UserProfile author = new UserProfile();
        author.setId(authorId);
        book.setAuthor(author);

        return bookProfileRepository.save(book);
    }

    @Transactional
    public BookProfile updateBookDescription(Long bookId, String description) {
        BookProfile book = getBookById(bookId);

        UUID authorId = book.getAuthor().getId();
        if (!authorizationService.hasAccess(authorId, "ADMIN") &&
                !authorizationService.hasAccess(authorId, null)) {
            throw new AccessDeniedException("Only author or admin can update book");
        }

        BookDescription bookDescription = book.getBookDescription();
        bookDescription.setDescription(description);
        bookDescriptionRepository.save(bookDescription);
        return book;
    }

    @Transactional
    public BookProfile updateBookProfile(Long bookId, BookProfile updatedBook, UUID currentUserId) {
        BookProfile existingBook = getBookById(bookId);

        // Проверка прав доступа
        if (!existingBook.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only author can update book profile");
        }

        // Обновление полей книги
        existingBook.setName(updatedBook.getName());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setYear(updatedBook.getYear());
        existingBook.setImageUrl(updatedBook.getImageUrl());

        // Обновление описания
        if (updatedBook.getBookDescription() != null) {
            BookDescription description = existingBook.getBookDescription();
            description.setDescription(updatedBook.getBookDescription().getDescription());
            bookDescriptionRepository.save(description);
        }

        return bookProfileRepository.save(existingBook);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        BookProfile book = getBookById(bookId);

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

    @Transactional(readOnly = true)
    public List<BookProfile> searchBooksByName(String query) {
        return bookProfileRepository.findByName(query);
    }
}