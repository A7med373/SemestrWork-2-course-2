package com.sem.models.user;

import com.sem.models.books.BookProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;

import java.util.UUID;

@Entity(name = "Users")
@NoArgsConstructor
@Getter
@Setter
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private Role role;

    @Column(unique = true)
    private BookProfile[] booksWritten;

    @Column
    @OneToMany(fetch = FetchType.LAZY)
    private BookProfile[] booksRead;

    @Column(name = "avatar_url")
    private String avatarUrl = "https://ui-avatars.com/api/?name=Default&background=random ";
}
