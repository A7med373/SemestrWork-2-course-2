package com.sem.models.books;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class BookDescription {
    @Id
    @Column
    private Long id;

    @Column
    @Lob
    @Getter
    @Setter
    private String text;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private BookProfile book;
}
