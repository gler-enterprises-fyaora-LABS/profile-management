package com.fyaora.profilemanagement.profileservice.model.db.entity;

import com.fyaora.profilemanagement.profileservice.model.enums.UserTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "UserType")
@Table(name = "user_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Primary Key

    @Enumerated(EnumType.STRING) // Store Enum as a String in the DB
    @Column(nullable = false)
    private UserTypeEnum type;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(nullable = false)
    private Boolean enabled;
}