package com.fyaora.profilemanagement.profileservice.model.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Waitlist")
@Table(name = "waitlist")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Waitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = UserTypeEnumConverter.class)
    @Column(name = "user_type", nullable = false)
    private UserTypeEnum userType;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telnum")
    private String telnum;

    @Column(name = "name")
    private String name;

    @Column(name = "postcode")
    private String postcode;

    @Convert(converter = VendorTypeConverter.class)
    @Column(name = "vendor_type")
    private VendorTypeEnum vendorType;

    @Column(nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "waitlist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WaitlistServiceOffered> waitlistServiceOffered = new ArrayList<>();
}
