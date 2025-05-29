package com.sem.models.books;

import jakarta.persistence.*;

@Entity
public class Description {
    @Id
    @Column
    private int id;

    @Column
    @Lob
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private BookProfile book;
}
