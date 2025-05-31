package com.sem.models.books;

import com.sem.models.user.UserProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity(name = "Books")
@NoArgsConstructor
@Setter
@Getter
public class BookProfile {
    @Id
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private Date year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserProfile author;

    @Column(name = "image_url")
    private String imageUrl;

    @Column
    @OneToOne(
            mappedBy = "book",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false
    )
    private BookDescription bookDescriptionId;
}
