package com.mthien.identity_service.payload.user;

import com.mthien.identity_service.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String email;
    String username;
    String firstName;
    String lastName;
    String avatar;
    String phone;
    String address;
    LocalDate dob;
    Boolean isActive;
    Boolean isGoogle;
    Boolean isFacebook;
    Boolean isVerified;
    String verificationToken;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    List<Role> roles;
}
