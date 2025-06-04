package com.sem.models.review;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sem.models.user.UserProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Review {
    @Id
    @Column
    private Long id;

    @Column
    @Lob
    private String review;

    @ManyToOne
    @JoinColumn(name = "userId")
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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = false)
    private Date updatedAt;
}
