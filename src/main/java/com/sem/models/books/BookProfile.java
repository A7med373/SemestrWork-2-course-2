package com.sem.models.books;

import com.sem.models.user.UserProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class BookProfile {
    @Id
    @Column
    private Long id;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column
    private String name;

    @Column
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserProfile author;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToOne(
            mappedBy = "book",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false
    )
    private BookDescription bookDescription;
}
