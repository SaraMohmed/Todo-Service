package com.example.userService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@ToString

public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq_gen")
    @SequenceGenerator(name = "my_seq_gen", sequenceName = "my_sequence", allocationSize = 1)
    @Column(name = "id")
    private int id;
    @Column(name = "otp",nullable = false)
    private int otp;
    @Column(name = "expiration_time",nullable = false)
    private Date expirationTime;
    @JsonIgnore
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
}
