package com.fyaora.profilemanagement.profileservice.model.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer did; // Primary Key

    @Enumerated(EnumType.STRING) // Store Enum as a String in the DB
    @Column(nullable = false)
    private UserTypeEnum type;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(nullable = false)
    private Boolean enabled;
}