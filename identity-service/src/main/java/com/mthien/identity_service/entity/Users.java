package com.mthien.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_username", columnList = "username")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(unique = true, nullable = false)
    String email;
    @Column(unique = true, nullable = false)
    String username;
    @Column(nullable = false)
    String password;
    @Column(columnDefinition = "NVARCHAR(255)")
    String firstName;
    @Column(columnDefinition = "NVARCHAR(255)")
    String lastName;
    String avatar;
    String phone;
    String address;
    LocalDate dob;
    boolean isActive;
    boolean isGoogle;
    boolean isFacebook;
    boolean isVerified;
    @Column(unique = true)
    String verificationToken;
    @CreationTimestamp
    LocalDateTime createAt;
    @UpdateTimestamp
    LocalDateTime updateAt;

    @ManyToMany
    Set<Role> roles;
}
