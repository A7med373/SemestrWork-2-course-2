package com.sem.models.books;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Entity(name = "Books")
@NoArgsConstructor
public class BookProfile {
    @Id
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private Date year;

    @Column
    @ManyToOne
    private UUID authorId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column
    @OneToOne(
            mappedBy = "book",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false
    )
    private Description descriptionId;
}
