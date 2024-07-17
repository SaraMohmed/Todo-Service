package com.example.todoService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "Items_details")
public class ItemsDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq_gen")
    @SequenceGenerator(name = "my_seq_gen", sequenceName = "my_sequence", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "status",columnDefinition = "default 'false'")
    private boolean status;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
