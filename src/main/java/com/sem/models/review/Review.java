package com.sem.models.review;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sem.models.user.User;
import com.sem.models.user.UserProfile;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Review")
@NoArgsConstructor
public class Review {
    @Id
    @Column
    private Long id;

    @Column
    @Lob
    private String review;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private UserProfile user;

    @OneToMany(
            mappedBy = "review",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("createdAt DESC")
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();
}
