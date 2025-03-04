package com.michaelnguyen.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
<<<<<<< HEAD
import jakarta.persistence.*;
=======

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
>>>>>>> origin/main
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email is mandatory")
    private String email;

    private String password;

    private String provider;

    private String providerId;

    private boolean enabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private UserProfile userProfile;

    @ManyToMany
    private Set<Role> roles;

}
